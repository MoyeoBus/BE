package com.moyeobus.websocket.domain.route

import java.time.LocalDateTime

data class RouteRequest (
    val id: Long? = null,
    val passengerId: Long,
    val routeId: Long? = null,
    val departure: Address,
    val destination: Address,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val status: RequestStatus
) {
    fun approve(): RouteRequest = copy(status = RequestStatus.APPROVED)
    fun assignRoute(id: Long): RouteRequest = copy(routeId = id)
}

enum class RequestStatus {PENDING, CANCELLED, APPROVED}


data class RouteRequestSummary (
    val totalCount: Long,
    val approvedCount: Long,
    val cancelledCount: Long,
    val pendingCount: Long
)