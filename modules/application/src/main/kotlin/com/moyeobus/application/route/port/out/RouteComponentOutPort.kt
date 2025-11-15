package com.moyeobus.application.route.port.out

import com.moyeobus.application.route.model.RouteItem
import com.moyeobus.domain.route.RouteComponent

interface RouteComponentOutPort {
    fun save(component: RouteComponent)
    fun saveAll(components: List<RouteComponent>)
    fun findById(id: Long) : RouteInfoWrapper
    fun findAllByRouteIdIn(routeIds: List<Long>): List<RouteComponent>
    fun findTimeRange(routeId: Long) : RouteTimeRange
    fun findAllByRouteId(routeId: Long) : List<RouteItem>
    fun countStations(routeId: Long): Int
}