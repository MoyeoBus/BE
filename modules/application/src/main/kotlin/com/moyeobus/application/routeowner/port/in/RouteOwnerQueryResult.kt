package com.moyeobus.application.routeowner.port.`in`

import com.moyeobus.application.routeowner.port.dto.PassengerRouteInfo

data class RouteOwnerQueryResult (
    val items: List<PassengerRouteInfo>,
    val nextCursor: String?,
    val hasNext: Boolean,
)