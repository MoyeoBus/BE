package com.moyeobus.application.routeowner.service

import com.moyeobus.application.route.port.`in`.QueryFilter
import com.moyeobus.application.route.port.out.RouteComponentOutPort
import com.moyeobus.application.route.port.out.RouteRequestOutPort
import com.moyeobus.application.routeowner.port.dto.PassengerRouteInfo
import com.moyeobus.application.routeowner.port.`in`.RouteOwnerQueryResult
import com.moyeobus.application.routeowner.port.`in`.RouteOwnerQueryUseCase
import com.moyeobus.application.routeowner.port.out.PassengerRouteOutPort
import com.moyeobus.application.routeowner.port.out.RouteOwnerQuery
import com.moyeobus.global.util.CursorUtil
import com.moyeobus.global.util.CursorWrapper
import com.moyeobus.global.util.DateTimeUtil
import org.springframework.stereotype.Service
import java.time.ZoneOffset


@Service
class RouteOwnerQueryService(
    private val passengerRouteRepository: PassengerRouteOutPort,
    private val routeComponentRepository: RouteComponentOutPort,
    private val routeRequestRepository: RouteRequestOutPort
) : RouteOwnerQueryUseCase {

    override fun query(passengerId: Long, filter: QueryFilter): RouteOwnerQueryResult {
        val decodedCursor = CursorUtil.decode(filter.cursor)

        val routeOwnerQuery = RouteOwnerQuery.from(passengerId, filter, decodedCursor)

        val queryItems = passengerRouteRepository.findBy(routeOwnerQuery)
        val items = queryItems.items

        val routeStatusMap = items.associate { it.route.id!! to it.route.status }
        val routeIds = routeStatusMap.keys.toList()

        val componentsByRouteId = routeComponentRepository
            .findAllByRouteIdIn(routeIds)
            .groupBy { it.routeId }



        val dtoList: List<PassengerRouteInfo> = routeIds.map { routeId ->
            val components = componentsByRouteId[routeId] ?: emptyList()
            val departure = components.firstOrNull()?.name ?: "출발지 미정"
            val destination = components.lastOrNull()?.name ?: "도착지 미정"

            val status = routeStatusMap[routeId] ?: "UNKNOWN"

            val routeRequestNames = routeRequestRepository.findDeparturesByPassengerAndRoute(passengerId, routeId)
            val assignedTimes = routeComponentRepository.findTimeByLocationAndRoute(
                routeRequestNames, routeId
            )

            val operatedDate = DateTimeUtil.formatDate(assignedTimes.first())
            val formattedTimes = assignedTimes
                .map { DateTimeUtil.formatTime(it) }


            PassengerRouteInfo(
                routeId = routeId,
                departure = departure,
                destination = destination,
                operatedDate = operatedDate,
                assignedTime = formattedTimes,
                status = status.toString()
            )
        }


        val nextCursorCreatedAt = queryItems.nextCursorCreatedAt
            ?.toInstant(ZoneOffset.UTC)

        return RouteOwnerQueryResult(
            items = dtoList,
            nextCursor = if (queryItems.hasNext) {
                CursorUtil.encode(CursorWrapper(nextCursorCreatedAt, queryItems.nextCursorId))
            } else null,
            hasNext = queryItems.hasNext,
        )
    }
}