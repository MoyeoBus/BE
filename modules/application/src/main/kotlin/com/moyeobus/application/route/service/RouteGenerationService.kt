package com.moyeobus.application.route.service

import com.moyeobus.application.address.dto.RouteDataWrapper
import com.moyeobus.application.address.port.out.AddressOutPort
import com.moyeobus.application.route.port.`in`.RouteGenerationUseCase
import com.moyeobus.application.route.port.out.RouteComponentOutPort
import com.moyeobus.application.route.port.out.RouteEngineOutPort
import com.moyeobus.application.route.port.out.RouteOutPort
import com.moyeobus.domain.route.Address
import com.moyeobus.domain.route.Route
import com.moyeobus.domain.route.RouteComponent
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class RouteGenerationService(
    private val routingEngine: RouteEngineOutPort,
    private val addressRepo: AddressOutPort,
    private val routeRepo: RouteOutPort,
    private val routeComponentRepo: RouteComponentOutPort
) : RouteGenerationUseCase {

    override fun generateRoute(): Route {
        val stops = addressRepo.findAll()
        require(stops.size >= 2) { "최소 2개 이상의 정류장이 필요합니다." }

        val routeData = routingEngine.calculatePath(stops)
        return persistGeneratedRoute(routeData)
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
            routeDistance = wrapper.routeDistance,
            routeTotalTime = wrapper.routeTotalTime,
            routeComponents = components
        )

        val savedRoute = routeRepo.save(route)
        persistRouteComponents(components, savedRoute)
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
}