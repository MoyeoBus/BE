package com.moyeobus.application.route.service

import com.moyeobus.application.address.port.out.AreaOutPort
import com.moyeobus.application.bus.port.out.BusOutPort
import com.moyeobus.application.route.model.RouteDetail
import com.moyeobus.application.route.model.RouteInfo
import com.moyeobus.application.route.port.`in`.LocalQueryFilter
import com.moyeobus.application.route.port.`in`.QueryFilter
import com.moyeobus.application.route.port.`in`.RouteQueryUseCase
import com.moyeobus.application.route.port.out.LocalRouteQuery
import com.moyeobus.application.route.port.out.RouteComponentOutPort
import com.moyeobus.application.route.port.out.RouteOutPort
import com.moyeobus.application.route.port.out.RouteRequestOutPort
import com.moyeobus.application.routeowner.port.`in`.RouteOwnerQueryResult
import com.moyeobus.application.routeowner.port.out.RouteOwnerQuery
import com.moyeobus.global.util.CursorUtil
import org.springframework.stereotype.Service

@Service
class RouteQueryService(
    private val cursorUtil: CursorUtil,
    private val areaRepo: AreaOutPort,
    private val busRepo: BusOutPort,
    private val routeRepo: RouteOutPort,
    private val routeComponentRepo: RouteComponentOutPort
) : RouteQueryUseCase {
    override fun queryRouteDetail(id: Long): RouteDetail {
        val route = routeRepo.findById(id)
        val busNumber = busRepo.findNumberById(route.busId!!)
        val routeTimeRange = routeComponentRepo.findTimeRange(id)
        val stations = routeComponentRepo.findAllByRouteId(id)

        val firstStationName = stations.firstOrNull()?.station ?: "UNKNOWN"
        val lastStationName = stations.lastOrNull()?.station ?: "UNKNOWN"

        val routeInfo = RouteInfo(
            busNumber = busNumber,
            departureName = firstStationName,
            destinationName = lastStationName,
            operateDate = routeTimeRange.date,
            departTime = routeTimeRange.departureTime,
            arrivalTime = routeTimeRange.destinationTime
        )

        return RouteDetail(routeInfo, stations)

    }

//    override fun queryLocalRoute(filter: LocalQueryFilter): RouteOwnerQueryResult {
//        val decodedCursor = cursorUtil.decode(filter.cursor)
//        val routeOwnerQuery = LocalRouteQuery.from(filter, decodedCursor)
//
//
//    }
}