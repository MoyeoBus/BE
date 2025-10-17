package com.moyeobus.application.route.port.`in`

import java.time.LocalDateTime

data class RouteCommand(
    val departureId: Long,
    val destinationId: Long,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
)
