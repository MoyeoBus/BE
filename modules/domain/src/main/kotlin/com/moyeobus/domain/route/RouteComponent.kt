package com.moyeobus.domain.route

import java.time.Instant

data class RouteComponent (
    val id: Long? = null,

    var route: Route?,

    val spot: Address,

    val assignedTime: Instant
) {
    fun assignRoute(route: Route) {
        this.route = route
    }
}