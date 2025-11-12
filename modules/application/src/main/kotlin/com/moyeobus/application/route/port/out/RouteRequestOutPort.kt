package com.moyeobus.application.route.port.out

import com.moyeobus.domain.route.Address
import com.moyeobus.domain.route.RouteRequest

interface RouteRequestOutPort {
    fun save(request: RouteRequest)
    fun findBy(query: RouteRequestQuery): RouteRequestPage
    fun findByAddress(addresses: List<Address>) : List<RouteRequest>
    fun findById(requestId: Long): RouteRequest
    fun findByPending() : List<RouteRequest>
    fun summary(filter: RouteRequestSummaryFilter) : RouteRequestSummaryProjection
}