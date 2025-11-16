package com.moyeobus.api.route.dto

import com.moyeobus.application.routeowner.port.dto.RouteInfoDto

data class QueryResponse(
    val items: List<RouteRequestResponse>,
    val summary: Summary,
    val nextCursor: String?,
    val hasNext: Boolean,
)

data class PassengerRouteQueryResponse(
    val items: List<RouteInfoDto>,
    val nextCursor: String?,
    val hasNext: Boolean,
)

data class LocalRouteQueryResponse(
    val items: List<RouteInfoDto>,
    val nextCursor: String?,
    val hasNext: Boolean,
)


data class Summary(
    val totalCount: Long,
    val approvedCount: Long,
    val cancelledCount: Long,
    val pendingCount: Long
)
