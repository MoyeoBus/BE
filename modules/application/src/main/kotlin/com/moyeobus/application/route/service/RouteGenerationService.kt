package com.moyeobus.application.route.service

import com.moyeobus.application.bus.port.out.BusOutPort
import com.moyeobus.application.route.port.`in`.RouteGenerationUseCase
import com.moyeobus.application.route.port.out.KakaoMobilityOutPort
import com.moyeobus.application.route.port.out.RouteComponentOutPort
import com.moyeobus.application.route.port.out.RouteOutPort
import com.moyeobus.application.route.port.out.RouteRequestOutPort
import com.moyeobus.application.route.port.out.dto.KakaoDirectionRequest
import com.moyeobus.application.route.port.out.dto.Point
import com.moyeobus.application.route.port.out.dto.Waypoint
import com.moyeobus.domain.bus.BusStatus
import com.moyeobus.domain.route.Address
import com.moyeobus.domain.route.GeoPoint
import com.moyeobus.domain.route.Route
import com.moyeobus.domain.route.RouteComponent
import com.moyeobus.domain.route.RouteRequest
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

    override fun generateRoute(): List<Any> {
        val groups = clusterByDistance()
        val kakaoResponse = mutableListOf<Any>()



        groups.forEachIndexed { index, group ->
            val request = KakaoDirectionRequest.fromCluster(group)
            val response = kakaoMobilityClient.getDirections(request)
            response.routes?.forEach { route ->
                val routeComponents = mutableListOf<RouteComponent>()
                val origin = route.summary?.origin
                val destination = route.summary?.destination
                val totalDistance = route.summary?.distance
                val totalDuration = route.summary?.duration
                val tempRoute = Route(
                id = null,
                operatorId = 1L,
                localGovId = 1L,
                busId = 1L,
                routeDistance = totalDistance ?: 0,
                routeTotalTime = totalDuration ?: 0,
                routeComponents = emptyList()
                )

                val routeEntity = routeRepo.save(tempRoute)



                route.sections?.forEach { section ->
                    val sectionDistance = section.distance
                    val sectionDuration = section.duration

                    section.guides?.forEach { guide ->
                        val name = guide.name
                        val x = guide.x
                        val y = guide.y
                        val guideDistance = guide.distance
                        val guideDuration = guide.duration
                        routeComponents.addLast(RouteComponent(
                            id = null,
                            route = routeEntity,
                            location = GeoPoint(x, y),
                            assignedTime = group.first().startDateTime
                        ))
                    }
                    routeComponentRepo.saveAll(routeComponents)
                }
            }


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
        val prevEnd = prev.destination
        val nextStart = next.departure

        val distMeters = distance(prevEnd, nextStart)

        return distMeters <= 2000
    }

    fun isSameSpot(a: RouteRequest, b: RouteRequest): Boolean {
        return a.departure.id == b.departure.id && a.destination.id == b.destination.id
    }

    fun isCloseTime(a: RouteRequest, b: RouteRequest, minutes: Long): Boolean {
        val diff = abs(
            a.startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() -
                    b.startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        return diff <= minutes * 60 * 1000
    }

    fun clusterByDistance(): List<List<RouteRequest>> {
        val requests = routeRequestRepo.findByPending()
            .sortedBy { it.startDateTime }

        val clusters = mutableListOf<MutableList<RouteRequest>>()

        for (req in requests) {
            var assigned = false

            for (group in clusters) {
                val last = group.last()

                // 1) 같은 장소 & 시간 비교 → 이전 요청만 유지
                if (isSameSpot(last, req) && isCloseTime(last, req, minutes = 30)) {
                    assigned = true
                    break // 현재 요청은 skip
                }

                // 2) 기존 그룹에 거리로 묶일 수 있다면 추가
                if (canBeInSameRouteByDistance(last, req)) {
                    group.add(req)
                    assigned = true
                    break
                }
            }

            // 3) 어떤 그룹에도 못 들어가면 새 그룹 생성
            if (!assigned) {
                clusters.add(mutableListOf(req))
            }
        }

        return clusters
    }



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