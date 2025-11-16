package com.moyeobus.application.route.port.`in`

import com.moyeobus.application.route.model.RouteDetail
import com.moyeobus.application.routeowner.port.`in`.RouteOwnerQueryResult

interface RouteQueryUseCase {
    fun queryRouteDetail(id: Long) : RouteDetail
    fun queryLocalRoute(filter: LocalQueryFilter) : RouteOwnerQueryResult
}