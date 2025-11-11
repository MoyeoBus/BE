package com.moyeobus.application.routeowner.port.`in`

import com.moyeobus.application.routeowner.port.dto.RouteInfoDto

data class RouteOwnerQueryResult (
    val items: List<RouteInfoDto>,
    val nextCursor: String?,
    val hasNext: Boolean,
)