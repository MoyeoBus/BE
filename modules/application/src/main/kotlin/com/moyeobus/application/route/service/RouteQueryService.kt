package com.moyeobus.application.route.service

import com.moyeobus.application.address.port.out.AreaOutPort
import com.moyeobus.application.bus.port.out.BusOutPort
import com.moyeobus.application.route.model.LocalRouteInfo
import com.moyeobus.application.route.model.RouteDetail
import com.moyeobus.application.route.model.RouteInfo
import com.moyeobus.application.route.port.`in`.LocalQueryFilter
import com.moyeobus.application.route.port.`in`.LocalRouteQueryResult
import com.moyeobus.application.route.port.`in`.RouteQueryUseCase
import com.moyeobus.application.route.port.out.LocalRouteQuery
import com.moyeobus.application.route.port.out.RouteComponentOutPort
import com.moyeobus.application.route.port.out.RouteOutPort
import com.moyeobus.global.util.CursorUtil

import com.moyeobus.global.util.CursorWrapper
import com.moyeobus.global.util.DateTimeUtil
import org.springframework.stereotype.Service
import java.time.ZoneOffset

@Service
class RouteQueryService(
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

    override fun queryLocalRoute(filter: LocalQueryFilter): LocalRouteQueryResult {
        val decodedCursor = CursorUtil.decode(filter.cursor)

        val dosiId = areaRepo.findDosiId(filter.dosi)
        val sigungu = areaRepo.findSigunguByDosi(dosiId, filter.sigungu)

        val localRouteQuery = LocalRouteQuery.from(sigungu.id!!,filter, decodedCursor)

        val queryItems = routeRepo.findBy(localRouteQuery)
        val items = queryItems.items

        val routeStatusMap = items.associate { it.id!! to it.status }
        val routeIds = routeStatusMap.keys.toList()


        val componentsByRouteId = routeComponentRepo
            .findAllByRouteIdIn(routeIds)
            .groupBy { it.routeId }

        val dtoList: List<LocalRouteInfo> = routeIds.map { routeId ->
            val components = componentsByRouteId[routeId] ?: emptyList()
            val departure = components.firstOrNull()?.name ?: "출발지 미정"
            val destination = components.lastOrNull()?.name ?: "도착지 미정"

            val operateDateStr = DateTimeUtil.formatDate(components.last().assignedTime)
            val departureTimeStr = DateTimeUtil.formatTime(components.first().assignedTime)
            val destinationTimeStr = DateTimeUtil.formatTime(components.last().assignedTime)


            val status = routeStatusMap[routeId] ?: "UNKNOWN"

            LocalRouteInfo(
                routeId = routeId,
                departure = departure,
                destination = destination,
                operateDate = operateDateStr,
                departureTime = departureTimeStr,
                destinationTime = destinationTimeStr,
                status = status.toString()
            )
        }

        val nextCursorCreatedAt = queryItems.nextCursorCreatedAt
            ?.toInstant(ZoneOffset.UTC)

        return LocalRouteQueryResult(
            items = dtoList,
            nextCursor = if (queryItems.hasNext) {
                CursorUtil.encode(CursorWrapper(nextCursorCreatedAt, queryItems.nextCursorId))
            } else null,
            hasNext = queryItems.hasNext,
        )
    }

    override fun queryStatus(id: Long): String {
        return routeRepo.findStatus(id)
    }
}