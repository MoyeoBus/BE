package com.moyeobus.application.route.model

import com.moyeobus.domain.route.GeoPoint

data class RouteTrackInfo(
    val routeId: Long,
    val nextStation: String,
    val nextStationPoint: GeoPoint,
    val gapTime: Int,
    val remainDistance: Int
)
