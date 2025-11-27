package com.moyeobus.application.route.service

import com.moyeobus.application.bus.port.out.BusOutPort
import com.moyeobus.application.route.port.`in`.RouteGenerationUseCase
import com.moyeobus.application.route.port.out.KakaoMobilityOutPort
import com.moyeobus.application.route.port.out.RouteComponentOutPort
import com.moyeobus.application.route.port.out.RouteOutPort
import com.moyeobus.application.route.port.out.RouteRequestCluster
import com.moyeobus.application.route.port.out.RouteRequestOutPort
import com.moyeobus.application.route.port.out.dto.KakaoDirectionRequest
import com.moyeobus.application.route.port.out.dto.KakaoDirectionResponse
import com.moyeobus.application.route.port.out.dto.Point
import com.moyeobus.application.route.port.out.dto.Waypoint
import com.moyeobus.application.routeowner.port.out.PassengerRouteOutPort
import com.moyeobus.application.user.port.out.PassengerOutPort
import com.moyeobus.domain.bus.BusStatus
import com.moyeobus.domain.route.Address
import com.moyeobus.domain.route.GeoPoint
import com.moyeobus.domain.route.Route
import com.moyeobus.domain.route.RouteComponent
import com.moyeobus.domain.route.RouteRequest
import com.moyeobus.domain.route.RouteStatus
import com.moyeobus.domain.routeowner.PassengerRoute
import org.springframework.stereotype.Service
import java.time.ZoneId
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Service
class RouteGenerationService(
    private val busRepo: BusOutPort,
    private val passengerRepo: PassengerOutPort,
    private val passengerRouteRepo: PassengerRouteOutPort,
    private val routeRepo: RouteOutPort,
    private val routeRequestRepo: RouteRequestOutPort,
    private val routeComponentRepo: RouteComponentOutPort,
    private val kakaoMobilityClient: KakaoMobilityOutPort
) : RouteGenerationUseCase {

    fun KakaoDirectionRequest.Companion.fromCluster(
        group: List<RouteRequest>
    ): KakaoDirectionRequest {
        val first = group.first().departure
        val last = group.last().destination

        val waypoints = group.drop(1).dropLast(1).map { req ->
            Waypoint("via", req.departure.lon, req.departure.lat)
        }

        return KakaoDirectionRequest(
            origin = Point(first.lon.toString(), first.lat.toString()),
            destination = Point(last.lon.toString(), last.lat.toString()),
            waypoints = waypoints
        )
    }

    override fun generateRoute(): List<Route> {
        val clusters = clusterByDistanceWithParticipants()
        val savedRoutes = mutableListOf<Route>()

        clusters.forEach { cluster ->
            val kakaoRoute = kakaoMobilityClient.getDirections(
                KakaoDirectionRequest.fromCluster(cluster.acceptedRequests)
            )

            val route = saveRoute(kakaoRoute, cluster)

            savePassengersOfRoute(route, cluster.exceptedRequests)

            savedRoutes.add(route)
            assignRouteToParticipants(route, cluster.exceptedRequests)
        }
        return savedRoutes
    }

    private fun assignRouteToParticipants(route: Route, requests: List<RouteRequest>) {
        requests.forEach { req ->
            val approved = req.approve()
            val withRoute = approved.assignRoute(route.id!!)
            routeRequestRepo.save(withRoute)
        }
    }

    private fun savePassengersOfRoute(route: Route, requests: List<RouteRequest>) {
        requests.forEach { req ->
            val passenger = passengerRepo.findById(req.passengerId!!)
            passengerRouteRepo.save(
                PassengerRoute(
                    id = null,
                    passenger = passenger,
                    route = route
                )
            )
        }
    }

    private fun createRouteComponents(
        response: KakaoDirectionResponse,
        route: Route,
        requests: List<RouteRequest>
    ): List<RouteComponent> {
        val components = mutableListOf<RouteComponent>()

        val representativeRequest = requests.first()
        val requestedNames = requests
            .flatMap { listOf(it.departure.name, it.destination.name) }
            .toSet()


        var currentTime = representativeRequest.startDateTime

        response.routes?.first()?.sections?.forEach { section ->
            section.guides?.forEach { guide ->
                val name = when (guide.name) {
                    "출발지" -> representativeRequest.departure.name
                    "도착지" -> representativeRequest.destination.name
                    else -> guide.name?.ifBlank { "경유지" } ?: "경유지"
                }

                val isRequested = name in requestedNames



                components.add(
                    RouteComponent(
                        id = null,
                        routeId = route.id!!,
                        name = name,
                        location = GeoPoint(guide.x, guide.y),
                        assignedTime = currentTime,
                        distance = guide.distance,
                        duration = guide.duration,
                        isRequested = isRequested
                    )
                )

                guide.duration?.let { duration ->
                    currentTime = currentTime.plusSeconds(duration.toLong())
                } ?: run {

                    section.duration?.let {
                        val guideCount = section.guides?.size ?: 1
                        currentTime = currentTime.plusSeconds((it / guideCount).toLong())
                    }
                }
            }
        }

        return components
    }

    private fun saveRoute(response: KakaoDirectionResponse, cluster: RouteRequestCluster): Route {
        val summary = response.routes?.first()?.summary
            ?: throw IllegalStateException("경로 요약 정보 없음")

        val route = Route(
            id = null,
            operatorId = 1L,
            localGovId = 1L,
            busId = 1L,
            routeDistance = summary.distance,
            routeTotalTime = summary.duration,
            status = RouteStatus.CREATED
        )

        val savedRoute = routeRepo.save(route)

        val components = createRouteComponents(response, savedRoute, cluster.acceptedRequests)

        routeComponentRepo.saveAll(components)

        return savedRoute
    }

    /**
     * 두 지점 간 거리 계산 함수.
     */
    fun distance(a: Address, b: Address): Double {
        val R = 6371e3 // 지구 반지름 (미터)
        val lat1 = Math.toRadians(a.lat)
        val lat2 = Math.toRadians(b.lat)
        val dLat = Math.toRadians(b.lat - a.lat)
        val dLon = Math.toRadians(b.lon - a.lon)

        val h = sin(dLat / 2).pow(2.0) +
                cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2.0)

        return 2 * R * atan2(sqrt(h), sqrt(1 - h)) // 단위: meter
    }

    /**
     * 두 지점 간 거리 계산을 통해 동일한 노선에 포함될 수 있는지 판단.
     */
    fun canBeInSameRouteByDistance(prev: RouteRequest, next: RouteRequest): Boolean {
        val prevEnd = prev.destination
        val nextStart = next.departure

        val distMeters = distance(prevEnd, nextStart)

        return distMeters <= 2000
    }

    /**
     * 동일한 출발지·도착지를 가진 요청이 짧은 시간 간격으로 중복 제출된 경우,
     * 이를 하나의 요청으로 취급하여 같은 승객 요청이 여러 노선에 중복 반영되지 않도록 한다.
     */
    fun isSameSpot(a: RouteRequest, b: RouteRequest): Boolean {
        return a.departure.id != null && b.departure.id != null &&
                a.destination.id != null && b.destination.id != null &&
                a.departure.id == b.departure.id && a.destination.id == b.destination.id
    }

    fun isCloseTime(a: RouteRequest, b: RouteRequest, minutes: Long): Boolean {
        val diff = abs(
            a.startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() -
                    b.startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        return diff <= minutes * 60 * 1000
    }

    fun clusterByDistanceWithParticipants(): List<RouteRequestCluster> {
        val requests = routeRequestRepo.findByPending()
            .sortedBy { it.startDateTime }

        val clusters = mutableListOf<MutableList<RouteRequest>>()
        val participantMap = mutableMapOf<Int, MutableList<RouteRequest>>()

        for (req in requests) {
            var assigned = false

            for ((index, group) in clusters.withIndex()) {
                val last = group.last()

                // 조건 1: 같은 위치 & 30분 내 요청 → 대표로는 안 들어가고 참여자로만 저장
                if (isSameSpot(last, req) && isCloseTime(last, req, 30)) {
                    participantMap.getOrPut(index) { mutableListOf() }.add(req)
                    assigned = true
                    break
                }

                // 조건 2: 거리 기준으로 같은 노선에 묶일 수 있을 때만 그룹에 포함
                if (canBeInSameRouteByDistance(last, req)) {
                    group.add(req)
                    assigned = true
                    break
                }
            }

            // 어떤 그룹에도 포함되지 않으면 새 그룹 생성
            if (!assigned) {
                clusters.add(mutableListOf(req))
            }
        }

        // 클러스터 + 참여자 묶음으로 반환
        return clusters.mapIndexed { index, group ->
            RouteRequestCluster(
                acceptedRequests = group,
                exceptedRequests = group + participantMap[index].orEmpty()
            )
        }
    }

//
//    /**
//     * RouteComponent 목록에 부모 Route 설정 후 저장
//     */
//    private fun persistRouteComponents(components: List<RouteComponent>, route: Route) {
//        components.forEach {
//            it.assignRoute(route)
//            routeComponentRepo.save(it)
//        }
//    }

    private fun persistBus(operatorId: Long, route: Route) {
        val buses = busRepo.findIdleBusesByOperatorId(operatorId)
        val selectedBus = buses.random()

        selectedBus.switch(BusStatus.OPERATING)
        selectedBus.id?.let { route.persistBus(it) }

        busRepo.save(selectedBus)
        routeRepo.save(route)
    }
}