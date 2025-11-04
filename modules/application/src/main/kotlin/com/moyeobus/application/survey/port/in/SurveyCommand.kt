package com.moyeobus.application.survey.port.`in`

import jakarta.validation.constraints.NotNull

data class SurveyCommand(
    @field:NotNull(message = "선택지 ID는 필수 값입니다.")
    val optionId: Long,
    @field:NotNull(message = "출발지 ID는 필수 값입니다.")
    val departureId: Long,
    @field:NotNull(message = "도착지 ID는 필수 값입니다.")
    val destinationId: Long
)
