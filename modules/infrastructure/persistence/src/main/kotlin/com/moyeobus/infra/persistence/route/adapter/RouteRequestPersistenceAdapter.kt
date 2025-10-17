package com.moyeobus.infra.persistence.route.adapter

import com.moyeobus.application.route.port.`in`.RouteRequestResult
import com.moyeobus.application.route.port.out.RouteRequestOutPort
import com.moyeobus.domain.route.RequestStatus
import com.moyeobus.domain.route.RouteRequest
import com.moyeobus.infra.persistence.route.entity.RouteRequestEntity
import com.moyeobus.infra.persistence.route.repository.RouteRequestJpaRepository
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

    override fun findBy(passengerId: Long): RouteRequestResult {
        val res = repo.pageBy(passengerId)
        return RouteRequestResult(items = res.map {it.toDomain()})
    }

    override fun findById(requestId: Long): RouteRequest {
        val res = repo.findById(requestId)
            .orElseThrow { IllegalArgumentException("RouteRequest not found: $requestId") }
        return res.toDomain()
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