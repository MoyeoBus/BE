package com.moyeobus.application.route.port.out

import com.moyeobus.application.localgov.port.`in`.LocalGovDateUseWrapper
import com.moyeobus.application.localgov.port.`in`.LocalGovStationWrapper
import com.moyeobus.application.localgov.port.`in`.LocalGovTimeUseWrapper
import com.moyeobus.application.route.model.RequestAddressCount
import com.moyeobus.domain.route.Address
import com.moyeobus.domain.route.RouteRequest
import java.time.LocalDate
import java.time.YearMonth

interface RouteRequestOutPort {
    fun save(request: RouteRequest)
    fun saveAll(request: List<RouteRequest>)
    fun countMonthly(requestIds: List<Long>, ym: YearMonth) : List<LocalGovDateUseWrapper>
    fun countHourly(requestIds: List<Long>, date: LocalDate) : List<LocalGovTimeUseWrapper>
    fun countDepartureByRouteId(routeId: Long) : List<LocalGovStationWrapper>
    fun countDestinationByRouteId(routeId: Long) : List<LocalGovStationWrapper>
    fun findBy(query: RouteRequestQuery): RouteRequestPage
    fun findByAddress(addresses: List<Address>) : List<RouteRequest>
    fun findById(requestId: Long): RouteRequest
    fun findByPending() : List<RouteRequest>
    fun findByRoutes(routeIds: List<Long?>) : List<RouteRequest>
     fun findDepartureCountByRoute(routeIds: List<Long?>) : List<RequestAddressCount>
    fun findDestinationCountByRoute(routeIds: List<Long?>) : List<RequestAddressCount>
    fun summary(filter: RouteRequestSummaryFilter) : RouteRequestSummaryProjection
}