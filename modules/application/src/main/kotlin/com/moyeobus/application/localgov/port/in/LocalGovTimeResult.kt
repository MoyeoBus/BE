package com.moyeobus.application.localgov.port.`in`

data class LocalGovTimeResult(
    val govName: String,
    val data: List<LocalGovTimeUseWrapper>
)
