package com.moyeobus.application.route.port.`in`

import com.moyeobus.domain.route.RouteRequest

data class RouteRequestResult(
    val items: List<RouteRequest>,
)