package com.moyeobus.application.route.service

import com.moyeobus.application.address.port.out.AddressOutPort
import com.moyeobus.application.route.port.`in`.RouteGenerationUseCase
import com.moyeobus.application.route.port.out.RouteEngineOutPort
import com.moyeobus.domain.route.Route
import org.springframework.stereotype.Service

@Service
class RouteGenerationService (
    private val routingEngine: RouteEngineOutPort,
    private val addressRepo: AddressOutPort
) : RouteGenerationUseCase {
    override fun generateRoute(): Route {
        val stops = addressRepo.findAll()
        require(stops.size >= 2) { "최소 2개 이상의 정류장이 필요합니다." }
        return routingEngine.calculatePath(stops)
    }
}