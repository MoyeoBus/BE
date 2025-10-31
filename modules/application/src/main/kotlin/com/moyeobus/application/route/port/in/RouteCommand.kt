package com.moyeobus.application.route.port.`in`

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class RouteCommand(
    @field:NotNull(message = "출발지 ID는 필수 값입니다.")
    val departureId: Long,
    @field:NotNull(message = "도착지 ID는 필수 값입니다.")
    val destinationId: Long,
    @field:NotNull(message = "출발 시각은 필수 값입니다.")
    val startDateTime: LocalDateTime,
    @field:NotNull(message = "도착 시각은 필수 값입니다.")
    val endDateTime: LocalDateTime
)
