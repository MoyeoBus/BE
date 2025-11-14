package com.moyeobus.application.transport.port.`in`

import com.moyeobus.application.route.model.RequestAreaRanking
import com.moyeobus.application.route.model.RequestStationRanking
import com.moyeobus.application.route.model.RouteDistanceRanking

interface TransportOperatorQueryUseCase {
    fun queryRouteLocalTop5(id: Long) : List<RequestAreaRanking>
    fun queryRouteStationTop5(id: Long) : List<RequestStationRanking>
    fun queryRouteDistanceTop5(id: Long) : List<RouteDistanceRanking>
}