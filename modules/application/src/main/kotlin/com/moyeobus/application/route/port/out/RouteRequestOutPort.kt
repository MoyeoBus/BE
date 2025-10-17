package com.moyeobus.application.route.port.out

import com.moyeobus.domain.route.RouteRequest

interface RouteRequestOutPort {
    fun save(request: RouteRequest)
    fun findBy(query: RouteRequestQuery): RouteRequestPage
    fun findById(requestId: Long): RouteRequest
    fun summary(filter: RouteRequestSummaryFilter) : RouteRequestSummaryProjection
}