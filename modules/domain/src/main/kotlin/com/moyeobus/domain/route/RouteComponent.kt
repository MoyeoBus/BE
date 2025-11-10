package com.moyeobus.domain.route

import java.time.LocalDateTime

data class RouteComponent (
    val id: Long? = null,

    var route: Route?,

    val name: String,

    val location: GeoPoint,

    val assignedTime: LocalDateTime
) {
    fun assignRoute(route: Route) {
        this.route = route
    }
}


data class GeoPoint(
    val lat: Double,
    val lon: Double
)