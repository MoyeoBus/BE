package com.moyeobus.application.route.port.`in`

import com.moyeobus.application.route.model.RouteDetail

interface RouteQueryUseCase {
    fun queryRouteDetail(id: Long) : RouteDetail
}