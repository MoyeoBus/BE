package com.moyeobus.application.localgov.port.`in`

import com.moyeobus.application.survey.model.RequestSurveySummary
import java.time.LocalDate
import java.time.YearMonth

interface LocalGovernmentUseCase {
    fun queryLocal(id: Long) : LocalGovStatusResult
    fun queryDate(id: Long, stdDate: YearMonth) : LocalGovDateResult
    fun queryHour(id: Long, date: LocalDate) : LocalGovTimeResult
    fun queryRoute(id: Long) : LocalGovRouteResult
    fun queryDeparture(routeId: Long) : List<LocalGovStationWrapper>
    fun queryDestination(routeId: Long) : List<LocalGovStationWrapper>
    fun querySurvey(id: Long) : List<RequestSurveySummary>
}