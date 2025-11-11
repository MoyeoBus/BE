package com.moyeobus.application.route.port.out

data class RouteInfoWrapper(
    val routeId: Long,
    val departure: String,
    val destination: String
)
