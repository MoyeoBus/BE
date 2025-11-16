package com.moyeobus.application.localgov.port.`in`

import com.moyeobus.application.route.model.RouteTrackInfo
import com.moyeobus.application.route.model.TrackItemOutput
import com.moyeobus.domain.route.GeoPoint
import java.time.LocalDate
import java.time.YearMonth

interface LocalGovernmentUseCase {
    fun queryLocal(id: Long) : LocalGovStatusResult
    fun queryDate(id: Long, stdDate: YearMonth) : LocalGovDateResult
    fun queryHour(id: Long, date: LocalDate) : LocalGovTimeResult
    fun queryRoute(id: Long) : LocalGovRouteResult
    fun queryDeparture(routeId: Long) : List<LocalGovStationWrapper>
    fun queryDestination(routeId: Long) : List<LocalGovStationWrapper>
    fun queryRouteTrackInfos(routeId: Long, currentStation: String) : RouteTrackInfo
    fun queryRouteTrackItems(routeId: Long) : List<TrackItemOutput>
    fun queryRouteTrackPoints(routeId: Long) : List<GeoPoint>
}