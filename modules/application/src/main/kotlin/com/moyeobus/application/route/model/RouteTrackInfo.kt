package com.moyeobus.application.route.model

data class RouteTrackInfo(
    val routeId: Long,
    val nextStation: String,
    val gapTime: Int,
    val remainDistance: Int
)
