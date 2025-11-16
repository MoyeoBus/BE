package com.moyeobus.api.route.dto

import com.moyeobus.application.route.model.LocalRouteInfo
import com.moyeobus.application.routeowner.port.dto.PassengerRouteInfo

data class QueryResponse(
    val items: List<RouteRequestResponse>,
    val summary: Summary,
    val nextCursor: String?,
    val hasNext: Boolean,
)

data class PassengerRouteQueryResponse(
    val items: List<PassengerRouteInfo>,
    val nextCursor: String?,
    val hasNext: Boolean,
)

data class LocalRouteQueryResponse(
    val items: List<LocalRouteInfo>,
    val nextCursor: String?,
    val hasNext: Boolean,
)


data class Summary(
    val totalCount: Long,
    val approvedCount: Long,
    val cancelledCount: Long,
    val pendingCount: Long
)
