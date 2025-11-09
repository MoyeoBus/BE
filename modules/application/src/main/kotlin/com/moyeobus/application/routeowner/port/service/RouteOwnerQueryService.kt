package com.moyeobus.application.routeowner.port.service

import com.moyeobus.application.route.port.`in`.QueryFilter
import com.moyeobus.application.route.port.`in`.QueryResult
import com.moyeobus.application.route.port.out.RouteRequestSummaryFilter
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
    private val passengerRouteRepository: PassengerRouteOutPort
) : RouteOwnerQueryUseCase {
    override fun query(id: Long, filter: QueryFilter): RouteOwnerQueryResult {
        val decodedCursor = cursorUtil.decode(filter.cursor)
        val routeOwnerQuery = RouteOwnerQuery.from(filter, decodedCursor)
        val queryItems = passengerRouteRepository.findBy(routeOwnerQuery)

        val nextCursorCreatedAt = queryItems.nextCursorCreatedAt
            ?.toInstant(ZoneOffset.UTC)

        return RouteOwnerQueryResult(
            items = queryItems.items,
            nextCursor = if (queryItems.hasNext) {
                cursorUtil.encode(CursorWrapper(nextCursorCreatedAt, queryItems.nextCursorId))
            } else null,
            hasNext = queryItems.hasNext,
        )
    }
}