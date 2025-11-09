package com.moyeobus.application.routeowner.port.dto

import com.moyeobus.domain.route.Route
import java.time.Instant

data class PassengerRouteDto(
    val id: Long? = null,

    val route: Route,

    var createdAt: Instant? = null
)
