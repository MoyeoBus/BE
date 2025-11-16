package com.moyeobus.application.routeowner.port.`in`

import com.moyeobus.application.route.port.`in`.QueryFilter

interface RouteOwnerQueryUseCase {
    fun query(passengerId: Long, filter: QueryFilter) : RouteOwnerQueryResult
}