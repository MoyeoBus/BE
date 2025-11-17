package com.moyeobus.application.transport.port.`in`

import com.moyeobus.application.localgov.port.`in`.OperationHistory
import com.moyeobus.application.route.model.BusUsageCount
import com.moyeobus.application.route.model.RequestAreaRanking
import com.moyeobus.application.route.model.RequestStationRanking
import com.moyeobus.application.route.model.RouteDistanceRanking
import com.moyeobus.application.route.model.RouteTrackInfo
import com.moyeobus.application.route.model.TrackItemOutput
import com.moyeobus.domain.route.GeoPoint

interface TransportOperatorQueryUseCase {
    fun queryRouteLocalTop5(id: Long) : List<RequestAreaRanking>
    fun queryRouteStationTop5(id: Long) : List<RequestStationRanking>
    fun queryRouteDistanceTop5(id: Long) : List<RouteDistanceRanking>
    fun queryBusUsage(operatorId: Long) : BusUsageCount
    fun queryTodayOperate(operatorId: Long) : Int
    fun queryHistory(operatorId: Long) : List<OperationHistory>
    fun queryRouteTrackInfos(routeId: Long, currentStation: String) : RouteTrackInfo
    fun queryRouteTrackItems(routeId: Long) : List<TrackItemOutput>
    fun queryRouteTrackPoints(routeId: Long) : List<GeoPoint>
}