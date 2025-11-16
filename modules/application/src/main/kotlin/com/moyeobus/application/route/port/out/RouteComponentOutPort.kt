package com.moyeobus.application.route.port.out

import com.moyeobus.application.route.model.RouteItem
import com.moyeobus.domain.route.RouteComponent
import java.time.LocalDateTime

interface RouteComponentOutPort {
    fun save(component: RouteComponent)
    fun saveAll(components: List<RouteComponent>)
    fun findById(id: Long) : RouteInfoWrapper
    fun findAllByRouteIdIn(routeIds: List<Long>): List<RouteComponent>
    fun findTimeRange(routeId: Long) : RouteTimeRange
    fun findAllByRouteId(routeId: Long) : List<RouteItem>
    fun findTimeByLocationAndRoute(names: List<String>, routeId: Long) : List<LocalDateTime>
    fun countStations(routeId: Long): Int
}