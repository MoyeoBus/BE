package com.moyeobus.application.route.service

import com.moyeobus.application.route.port.`in`.QueryFilter
import com.moyeobus.application.route.port.`in`.QueryResult
import com.moyeobus.application.route.port.`in`.RouteRequestQueryUseCase
import com.moyeobus.application.route.port.out.RouteRequestCursorWrapper
import com.moyeobus.application.route.port.out.RouteRequestOutPort
import com.moyeobus.application.route.port.out.RouteRequestQuery
import com.moyeobus.application.route.port.out.RouteRequestSummaryFilter
import com.moyeobus.domain.route.RouteRequestSummary
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneOffset
import java.util.Base64

@Service
class RouteRequestQueryService(
    private val routeRequestRepository: RouteRequestOutPort
) : RouteRequestQueryUseCase {
    override fun query(passengerId: Long, filter: QueryFilter): QueryResult {
        val decodedCursor = decodeCursor(filter.cursor)
        val routeRequestQuery = RouteRequestQuery.from(passengerId, filter, decodedCursor)
        val queryItems = routeRequestRepository.findBy(routeRequestQuery)

        val nextCursorCreatedAt = queryItems.nextCursorCreatedAt
            ?.toInstant(ZoneOffset.UTC)

        val routeRequestSummaryFilter = RouteRequestSummaryFilter.from(filter)
        val routeRequestSummary = routeRequestRepository.summary(routeRequestSummaryFilter)

        return QueryResult(
            items = queryItems.items,
            summary = RouteRequestSummary(
                routeRequestSummary.totalCount, routeRequestSummary.approvedCount,
                routeRequestSummary.cancelledCount, routeRequestSummary .pendingCount
            ),
            nextCursor = if (queryItems.hasNext) {
                encodeCursor(nextCursorCreatedAt, queryItems.nextCursorId)
            } else null,
            hasNext = queryItems.hasNext,
        )
    }

    private fun encodeCursor(createdAt: Instant?, id: Long?): String? {
        if (createdAt == null || id == null) return null
        val raw = "${createdAt.toEpochMilli()}:$id"
        return Base64.getUrlEncoder().withoutPadding().encodeToString(raw.toByteArray())
    }

    private fun decodeCursor(cursor: String?): RouteRequestCursorWrapper {
        if (cursor.isNullOrBlank()) return RouteRequestCursorWrapper(null, null)

        return try {
            val raw = String(Base64.getUrlDecoder().decode(cursor))
            val parts = raw.split(":")
            val ts = parts[0].toLong()
            val id = parts[1].toLong()
            RouteRequestCursorWrapper(
                cursorCreatedAt = Instant.ofEpochMilli(ts),
                cursorId = id
            )
        } catch (e: Exception) {
            RouteRequestCursorWrapper(null, null)
        }
    }
}