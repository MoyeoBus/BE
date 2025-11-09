package com.moyeobus.application.routeowner.port.`in`

import com.moyeobus.application.routeowner.port.dto.PassengerRouteDto

data class RouteOwnerQueryResult (
    val items: List<PassengerRouteDto>,
    val nextCursor: String?,
    val hasNext: Boolean,
)