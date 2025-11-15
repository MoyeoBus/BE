package com.moyeobus.domain.route

import java.time.LocalDateTime

data class RouteComponent (
    val id: Long? = null,

    var routeId: Long,

    val name: String,

    val location: GeoPoint,

    val assignedTime: LocalDateTime,

    val isRequested: Boolean
)


data class GeoPoint(
    val lat: Double,
    val lon: Double
)