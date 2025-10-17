package com.moyeobus.application.route.port.out

data class RouteRequestSummaryProjection(
    val totalCount: Long,
    val approvedCount: Long,
    val cancelledCount: Long,
    val pendingCount: Long
)
