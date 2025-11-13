package com.moyeobus.application.localgov.port.`in`

import java.time.LocalDate

data class LocalGovDateUseWrapper(
    val date: LocalDate,
    val useCount: Int,
)
