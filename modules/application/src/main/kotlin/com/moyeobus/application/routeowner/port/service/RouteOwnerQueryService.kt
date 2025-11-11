package com.moyeobus.application.routeowner.port.service

import com.moyeobus.application.route.port.`in`.QueryFilter
import com.moyeobus.application.route.port.out.RouteComponentOutPort
import com.moyeobus.application.route.port.out.RouteOutPort
import com.moyeobus.application.routeowner.port.dto.RouteInfoDto
import com.moyeobus.application.routeowner.port.`in`.RouteOwnerQueryResult
import com.moyeobus.application.routeowner.port.`in`.RouteOwnerQueryUseCase
import com.moyeobus.application.routeowner.port.out.PassengerRouteOutPort
import com.moyeobus.application.routeowner.port.out.RouteOwnerQuery
import com.moyeobus.global.util.CursorUtil
import com.moyeobus.global.util.CursorWrapper
import org.springframework.stereotype.Service
import java.time.ZoneOffset

@Service
class RouteOwnerQueryService(
    private val cursorUtil: CursorUtil,
    private val passengerRouteRepository: PassengerRouteOutPort,
    private val routeComponentRepository: RouteComponentOutPort,
    private val routeRepository: RouteOutPort
) : RouteOwnerQueryUseCase {

    override fun query(id: Long, filter: QueryFilter): RouteOwnerQueryResult {
        val decodedCursor = cursorUtil.decode(filter.cursor)
        val routeOwnerQuery = RouteOwnerQuery.from(id, filter, decodedCursor)

        val queryItems = passengerRouteRepository.findBy(routeOwnerQuery)
        val items = queryItems.items

        val routeStatusMap = items.associate { it.route.id!! to it.route.status }
        val routeIds = routeStatusMap.keys.toList()


        val componentsByRouteId = routeComponentRepository
            .findAllByRouteIdIn(routeIds)
            .groupBy { it.routeId }



        val dtoList: List<RouteInfoDto> = routeIds.map { routeId ->
            val components = componentsByRouteId[routeId] ?: emptyList()
            val departure = components.firstOrNull()?.name ?: "출발지 미정"
            val destination = components.lastOrNull()?.name ?: "도착지 미정"
            val status = routeStatusMap[routeId] ?: "UNKNOWN"

            RouteInfoDto(
                routeId = routeId,
                departure = departure,
                destination = destination,
                status = status.toString()
            )
        }


        val nextCursorCreatedAt = queryItems.nextCursorCreatedAt
            ?.toInstant(ZoneOffset.UTC)

        return RouteOwnerQueryResult(
            items = dtoList,
            nextCursor = if (queryItems.hasNext) {
                cursorUtil.encode(CursorWrapper(nextCursorCreatedAt, queryItems.nextCursorId))
            } else null,
            hasNext = queryItems.hasNext,
        )
    }
}