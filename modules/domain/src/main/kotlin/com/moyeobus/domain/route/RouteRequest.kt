package com.moyeobus.domain.route

import java.time.LocalDateTime

data class RouteRequest (
    val id: Long? = null,
    val passengerId: Long? = null,
    val departureId: Long,
    val destinationId: Long,
    var startDateTime: LocalDateTime,
    var endDateTime: LocalDateTime,
    var status: RequestStatus
) {
    fun approve(): RouteRequest = copy(status = RequestStatus.APPROVED)
    fun cancel(): RouteRequest = copy(status = RequestStatus.CANCELLED)
}



enum class RequestStatus {PENDING, CANCELLED, APPROVED}