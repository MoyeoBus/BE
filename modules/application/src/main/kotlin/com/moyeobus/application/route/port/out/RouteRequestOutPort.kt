package com.moyeobus.application.route.port.out

import com.moyeobus.application.route.port.`in`.RouteRequestResult
import com.moyeobus.domain.route.RouteRequest

interface RouteRequestOutPort {
    fun save(request: RouteRequest)
    fun findBy(passengerId: Long): RouteRequestResult
    fun findById(requestId: Long): RouteRequest
}