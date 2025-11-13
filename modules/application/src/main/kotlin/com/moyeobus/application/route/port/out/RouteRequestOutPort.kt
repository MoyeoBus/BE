package com.moyeobus.application.route.port.out

import com.moyeobus.application.localgov.port.`in`.LocalGovDateUseWrapper
import com.moyeobus.application.localgov.port.`in`.LocalGovTimeUseWrapper
import com.moyeobus.domain.route.Address
import com.moyeobus.domain.route.RouteRequest
import java.time.LocalDate

interface RouteRequestOutPort {
    fun save(request: RouteRequest)
    fun saveAll(request: List<RouteRequest>)
    fun countMonthly(requestIds: List<Long>) : List<LocalGovDateUseWrapper>
    fun countHourly(requestIds: List<Long>, date: LocalDate) : List<LocalGovTimeUseWrapper>
    fun findBy(query: RouteRequestQuery): RouteRequestPage
    fun findByAddress(addresses: List<Address>) : List<RouteRequest>
    fun findById(requestId: Long): RouteRequest
    fun findByPending() : List<RouteRequest>
    fun summary(filter: RouteRequestSummaryFilter) : RouteRequestSummaryProjection
}