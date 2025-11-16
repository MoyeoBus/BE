package com.moyeobus.application.route.port.`in`

import com.moyeobus.application.route.model.LocalRouteInfo

data class LocalRouteQueryResult(
    val items: List<LocalRouteInfo>,
    val nextCursor: String?,
    val hasNext: Boolean,
)
