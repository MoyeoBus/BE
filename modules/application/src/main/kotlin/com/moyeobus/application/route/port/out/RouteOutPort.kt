package com.moyeobus.application.route.port.out

import com.moyeobus.application.route.model.BusUsageCount
import com.moyeobus.domain.route.Route

interface RouteOutPort {
    fun save(route: Route): Route
    fun findById(id: Long): Route
    fun findStatus(id: Long): String
    fun findByLocal(id: Long): List<Route>
    fun findNotCompletedByOperator(id: Long) : List<Route>
    fun findBy(query: LocalRouteQuery): LocalRoutePage
    fun findByOperator(id: Long): List<Route>
    fun countBusUsage(operatorId: Long): BusUsageCount
}