package com.moyeobus.infra.persistence.route.adapter

import com.moyeobus.application.route.port.`in`.RouteRequestResult
import com.moyeobus.application.route.port.out.RouteRequestOutPort
import com.moyeobus.application.route.port.out.RouteRequestPage
import com.moyeobus.application.route.port.out.RouteRequestQuery
import com.moyeobus.application.route.port.out.RouteRequestSummaryFilter
import com.moyeobus.application.route.port.out.RouteRequestSummaryProjection
import com.moyeobus.domain.route.RequestStatus
import com.moyeobus.domain.route.RouteRequest
import com.moyeobus.infra.persistence.route.entity.RouteRequestEntity
import com.moyeobus.infra.persistence.route.repository.RouteRequestJpaRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class RouteRequestPersistenceAdapter(
    private val repo: RouteRequestJpaRepository
) : RouteRequestOutPort {
    override fun save(request: RouteRequest) {
        repo.save(request.toEntity())
    }

    override fun findBy(query: RouteRequestQuery): RouteRequestPage {
        val pageSize = query.limit
        val list = repo.pageBy(
            passengerId = 1L,
            status = query.status?.name,
            fromAt = query.from?.toInstant(ZoneOffset.UTC),
            toAt = query.to?.toInstant(ZoneOffset.UTC),
            cursorCreatedAt = query.cursorCreatedAt?.toInstant(ZoneOffset.UTC),
            cursorId = query.cursorId,
            org = PageRequest.of(0, pageSize + 1),
        )

        val hasNext = list.size > pageSize
        val items = list.take(pageSize)
        val last = items.lastOrNull()

        return RouteRequestPage(
            items = items.map { it.toDomain() },
            hasNext = hasNext,
            nextCursorCreatedAt = last?.createdAt?.let { java.time.LocalDateTime.ofInstant(it, ZoneOffset.UTC) },
            nextCursorId = last?.id,
        )
    }


    override fun findById(requestId: Long): RouteRequest {
        val res = repo.findById(requestId)
            .orElseThrow { IllegalArgumentException("RouteRequest not found: $requestId") }
        return res.toDomain()
    }

    override fun summary(filter: RouteRequestSummaryFilter): RouteRequestSummaryProjection {
        val list = repo.summary(
            passengerId = 1L,
            status = filter.status?.name,
            fromAt = filter.from?.toInstant(ZoneOffset.UTC),
            toAt = filter.to?.toInstant(ZoneOffset.UTC),
        )
        val arr = list.first()
        val totalCount = (arr[0] as Number).toLong()
        val approvedCount = (arr[1] as Number).toLong()
        val cancelledCount = (arr[2] as Number).toLong()
        val pendingCount = (arr[3] as Number).toLong()
        return RouteRequestSummaryProjection(totalCount, approvedCount
            , cancelledCount, pendingCount)
    }

    private fun RouteRequest.toEntity() =
        RouteRequestEntity(
            id = this.id,
            passengerId = 1L,
            departureId = this.departureId,
            destinationId = this.destinationId,
            startDateTime = this.startDateTime.toInstant(ZoneOffset.UTC),
            endDateTime = this.endDateTime.toInstant(ZoneOffset.UTC),
            status = this.status.name
        )
    private fun RouteRequestEntity.toDomain() =
        RouteRequest(
            id = this.id,
            passengerId = this.passengerId,
            departureId = this.departureId,
            destinationId = this.destinationId,
            startDateTime = LocalDateTime.ofInstant(this.startDateTime, ZoneOffset.UTC),
            endDateTime = LocalDateTime.ofInstant(this.endDateTime, ZoneOffset.UTC),
            status = RequestStatus.valueOf(this.status)
        )
}