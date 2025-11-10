package com.moyeobus.application.route.port.out

data class RouteInfoDto(
    val routeId: Long,
    val departure: String,
    val destination: String
)
