package com.moyeobus.scheduler.application.route.port.out

import com.moyeobus.scheduler.domain.route.RouteRequest

interface RouteRequestOutPort {
    fun save(request: RouteRequest)
    fun findByPending() : List<RouteRequest>
}