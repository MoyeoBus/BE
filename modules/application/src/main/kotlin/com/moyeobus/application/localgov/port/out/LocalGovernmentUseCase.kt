package com.moyeobus.application.localgov.port.out

import com.moyeobus.application.localgov.port.`in`.LocalGovDateResult
import com.moyeobus.application.localgov.port.`in`.LocalGovRouteResult
import com.moyeobus.application.localgov.port.`in`.LocalGovStatusResult
import com.moyeobus.application.localgov.port.`in`.LocalGovTimeResult
import java.time.LocalDate


interface LocalGovernmentUseCase {
    fun queryLocal(id: Long) : LocalGovStatusResult
    fun queryDate(id: Long) : LocalGovDateResult
    fun queryHour(id: Long, date: LocalDate) : LocalGovTimeResult
    fun queryRoute(id: Long) : LocalGovRouteResult
}