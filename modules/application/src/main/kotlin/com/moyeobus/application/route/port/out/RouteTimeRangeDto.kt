package com.moyeobus.application.route.port.out

import java.time.LocalDateTime

data class RouteTimeRangeDto(
    val routeId: Long,
    val departureTime: LocalDateTime?,
    val arrivalTime: LocalDateTime?
)
