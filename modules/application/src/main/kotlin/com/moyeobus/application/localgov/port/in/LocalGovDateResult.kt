package com.moyeobus.application.localgov.port.`in`

data class LocalGovDateResult(
    val govName: String,
    val data: List<LocalGovDateUseWrapper>
)
