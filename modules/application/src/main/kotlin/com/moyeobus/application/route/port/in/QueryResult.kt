package com.moyeobus.application.route.port.`in`

import com.moyeobus.domain.route.RouteRequest
import com.moyeobus.domain.route.RouteRequestSummary

data class QueryResult(
    val items: List<RouteRequest>,
    val summary: RouteRequestSummary,
    val nextCursor: String?,
    val hasNext: Boolean,
)
