package com.moyeobus.domain.route

import java.time.Instant

data class RouteComponent (
    val id: Long? = null,

    val routeId: Long,

    val assignedTime: Instant
)