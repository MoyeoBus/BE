package com.moyeobus.application.route.port.`in`

interface RouteRequestQueryUseCase {
    fun query(passengerId: Long,filter: QueryFilter): QueryResult
}