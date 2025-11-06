package com.moyeobus.application.route.service

import com.moyeobus.application.address.dto.RouteDataWrapper
import com.moyeobus.application.address.port.out.AddressOutPort
import com.moyeobus.application.bus.port.out.BusOutPort
import com.moyeobus.application.route.port.`in`.RouteGenerationUseCase
import com.moyeobus.application.route.port.out.KakaoMobilityOutPort
import com.moyeobus.application.route.port.out.RouteComponentOutPort
import com.moyeobus.application.route.port.out.RouteEngineOutPort
import com.moyeobus.application.route.port.out.RouteOutPort
import com.moyeobus.application.route.port.out.RouteRequestOutPort
import com.moyeobus.application.route.port.out.dto.KakaoDirectionRequest
import com.moyeobus.application.route.port.out.dto.Point
import com.moyeobus.application.route.port.out.dto.Waypoint
import com.moyeobus.domain.bus.BusStatus
import com.moyeobus.domain.route.Address
import com.moyeobus.domain.route.Route
import com.moyeobus.domain.route.RouteComponent
import com.moyeobus.domain.route.RouteRequest
import org.springframework.stereotype.Service
import java.time.Instant
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Service
class RouteGenerationService(
    private val routingEngine: RouteEngineOutPort,
    private val addressRepo: AddressOutPort,
    private val busRepo: BusOutPort,
    private val routeRepo: RouteOutPort,
    private val routeRequestRepo: RouteRequestOutPort,
    private val routeComponentRepo: RouteComponentOutPort,
    private val kakaoMobilityClient: KakaoMobilityOutPort
) : RouteGenerationUseCase {

    fun KakaoDirectionRequest.Companion.fromCluster(
        group: List<RouteRequest>,
        addressRepo: AddressOutPort
    ): KakaoDirectionRequest {
        val first = addressRepo.findById(group.first().departure.id!!)
        val last = addressRepo.findById(group.last().destination.id!!)

        val waypoints = group.drop(1).dropLast(1).map { req ->
            addressRepo.findById(req.departure.id!!).let { addr ->
                Waypoint("via", addr.lon, addr.lat)
            }
        }

        return KakaoDirectionRequest(
            origin = Point(first.lon.toString(), first.lat.toString()),
            destination = Point(last.lon.toString(), last.lat.toString()),
            waypoints = waypoints
        )
    }

    override fun generateRoute(): List<Any> {
        val groups = clusterByDistance()
        val createdRoutes = mutableListOf<Route>()
        val kakaoResponse = mutableListOf<Any>()

        groups.forEach { group ->
            val request = KakaoDirectionRequest.fromCluster(group, addressRepo)
            val response = kakaoMobilityClient.getDirections(request)

            //val route = saveRoute(response, group) // Route + RouteComponents 저장
            //createdRoutes.add(route)
            kakaoResponse.add(response)
        }

        return kakaoResponse
    }


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

    fun canBeInSameRouteByDistance(prev: RouteRequest, next: RouteRequest): Boolean {
        val prevEnd = addressRepo.findById(prev.destination.id!!)
        val nextStart = addressRepo.findById(next.departure.id!!)

        val distMeters = distance(prevEnd, nextStart)

        return distMeters <= 2000
    }

    fun clusterByDistance(): List<List<RouteRequest>> {
        val requests = routeRequestRepo.findByPending().sortedBy { it.startDateTime }
        val clusters = mutableListOf<MutableList<RouteRequest>>()

        for (req in requests) {
            var assigned = false

            // 이미 존재하는 노선에 붙일 수 있으면 추가
            for (group in clusters) {
                if (canBeInSameRouteByDistance(group.last(), req)) {
                    group.add(req)
                    assigned = true
                    break
                }
            }

            if (!assigned) {
                clusters.add(mutableListOf(req))  // 새로운 노선 생성
            }
        }

        return clusters
    }

    fun kakao(){

    }

    /**
     * 라우팅 엔진 결과를 기반으로 Route 및 RouteComponent 저장
     */
    private fun persistGeneratedRoute(wrapper: RouteDataWrapper): Route {
        val components = wrapper.stops.map { createRouteComponent(it) }
        val route = Route(
            id = null,
            operatorId = 1L,
            localGovId = 1L,
            busId = null,
            routeDistance = wrapper.routeDistance,
            routeTotalTime = wrapper.routeTotalTime,
            routeComponents = components
        )

        val savedRoute = routeRepo.save(route)
        persistRouteComponents(components, savedRoute)
        persistBus(route.operatorId, route)
        return savedRoute
    }

    /**
     * 정류장을 기반으로 임시 RouteComponent 생성
     */
    private fun createRouteComponent(stop: Address) = RouteComponent(
        id = null,
        route = null,
        spot = stop,
        assignedTime = Instant.now() // TODO: 실제 운행 시간 반영 예정
    )

    /**
     * RouteComponent 목록에 부모 Route 설정 후 저장
     */
    private fun persistRouteComponents(components: List<RouteComponent>, route: Route) {
        components.forEach {
            it.assignRoute(route)
            routeComponentRepo.save(it)
        }
    }

    private fun persistBus(operatorId: Long, route: Route) {
        val buses = busRepo.findIdleBusesByOperatorId(operatorId)
        val selectedBus = buses.random()

        selectedBus.switch(BusStatus.OPERATING)
        selectedBus.id?.let { route.persistBus(it) }

        busRepo.save(selectedBus)
        routeRepo.save(route)
    }
}