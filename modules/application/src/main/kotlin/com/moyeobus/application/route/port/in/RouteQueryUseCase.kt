package com.moyeobus.application.route.port.`in`

import com.moyeobus.application.route.model.RouteDetail

interface RouteQueryUseCase {
    fun queryRouteDetail(id: Long) : RouteDetail
    fun queryLocalRoute(filter: LocalQueryFilter) : LocalRouteQueryResult
    fun queryStatus(id: Long) : String
}