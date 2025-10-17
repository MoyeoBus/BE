package com.moyeobus.application.route.port.`in`

interface RouteRequestQueryUseCase {
    fun query(filter: QueryFilter): QueryResult
}