package com.moyeobus.api.route.dto

data class QueryResponse(
    val items: List<RouteRequestResponse>,
    val nextCursor: String?,
    val hasNext: Boolean,
)
