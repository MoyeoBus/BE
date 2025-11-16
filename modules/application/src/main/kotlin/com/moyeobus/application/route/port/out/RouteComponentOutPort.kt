package com.moyeobus.application.route.port.out

import com.moyeobus.application.route.model.RouteItem
import com.moyeobus.application.route.model.RouteTrackInfo
import com.moyeobus.application.route.model.TrackItemOutput
import com.moyeobus.domain.route.GeoPoint
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
    fun findTrackInfo(routeId: Long, currentStation: String) : RouteTrackInfo
    fun findTrackItems(routeId: Long) : List<TrackItemOutput>
    fun findTrackPoints(routeId: Long) : List<GeoPoint>
    fun countStations(routeId: Long): Int
}