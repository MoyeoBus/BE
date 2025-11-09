package com.moyeobus.api.route.dto

import com.moyeobus.application.routeowner.port.dto.PassengerRouteDto
import com.moyeobus.domain.route.Route

data class PassengerRouteResponse(
    val id: Long? = null,
    val route: Route
) {
    companion object {
        fun from (r: PassengerRouteDto) = PassengerRouteResponse(
            id = r.id,
            route = r.route
        )
    }
}

