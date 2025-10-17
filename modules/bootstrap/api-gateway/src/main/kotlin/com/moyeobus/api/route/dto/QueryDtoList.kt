package com.moyeobus.api.route.dto

data class QueryResponse(
    val items: List<RouteRequestResponse>,
    val summary: Summary,
    val nextCursor: String?,
    val hasNext: Boolean,
)

data class Summary(
    val totalCount: Long,
    val approvedCount: Long,
    val cancelledCount: Long,
    val pendingCount: Long
)
