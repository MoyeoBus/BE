package com.moyeobus.infra.persistence.routeowner.dto

import com.moyeobus.infra.persistence.route.entity.RouteEntity
import java.time.Instant

data class PassengerRouteEntityDto(
    val id: Long? = null,

    val route: RouteEntity,

    var createdAt: Instant? = null
)
