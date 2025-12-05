package com.moyeobus.scheduler.domain.route

import java.time.LocalDateTime

data class RouteComponent (
    val id: Long? = null,

    var routeId: Long,

    val name: String,

    val location: GeoPoint,

    val assignedTime: LocalDateTime,

    val distance: Int,

    val duration: Int,

    val isRequested: Boolean
)


data class GeoPoint(
    val lat: Double,
    val lon: Double
)