package com.moyeobus.application.localgov.port.`in`

data class OperationHistory(
    val routeId: Long,
    val departureNm: String,
    val destinationNm: String,
    val status: String
)