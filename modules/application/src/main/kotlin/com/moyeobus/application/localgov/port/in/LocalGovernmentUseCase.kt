package com.moyeobus.application.localgov.port.`in`

import java.time.LocalDate

interface LocalGovernmentUseCase {
    fun queryLocal(id: Long) : LocalGovStatusResult
    fun queryDate(id: Long) : LocalGovDateResult
    fun queryHour(id: Long, date: LocalDate) : LocalGovTimeResult
    fun queryRoute(id: Long) : LocalGovRouteResult
}