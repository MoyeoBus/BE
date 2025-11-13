package com.moyeobus.application.localgov.port.`in`

data class LocalGovRouteWrapper(
    val routeId: Long,
    val stationCount: Int,
    val peopleCount: Int,
    val distance: Int
)