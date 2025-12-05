package com.moyeobus.scheduler.infra.persistence.route.adapter

import com.moyeobus.scheduler.application.route.port.out.RouteRequestOutPort
import com.moyeobus.scheduler.domain.route.RequestStatus
import com.moyeobus.scheduler.domain.route.RouteRequest
import com.moyeobus.scheduler.infra.persistence.address.mapper.AddressMapper
import com.moyeobus.scheduler.infra.persistence.route.entity.RouteRequestEntity
import com.moyeobus.scheduler.infra.persistence.route.repository.RouteRequestJpaRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.collections.map

@Component
class RouteRequestPersistenceAdapter(
    private val addressMapper: AddressMapper,
    private val repo: RouteRequestJpaRepository
) : RouteRequestOutPort {
    override fun save(request: RouteRequest) {
        repo.save(request.toEntity())
    }

    override fun findByPending(): List<RouteRequest> {
        val res = repo.findByStatus(RequestStatus.PENDING.toString())
        return res.map { it.toDomain() }
    }


    private fun RouteRequest.toEntity() =
        RouteRequestEntity(
            id = this.id,
            passengerId = this.passengerId,
            routeId = this.routeId,
            departure = addressMapper.toEntity(this.departure),
            destination = addressMapper.toEntity(this.destination),
            startDateTime = this.startDateTime.toInstant(ZoneOffset.UTC),
            endDateTime = this.endDateTime.toInstant(ZoneOffset.UTC),
            status = this.status.name
        )
    private fun RouteRequestEntity.toDomain() =
        RouteRequest(
            id = this.id,
            passengerId = this.passengerId,
            routeId = this.routeId,
            departure = addressMapper.toDomain(this.departure),
            destination = addressMapper.toDomain(this.destination),
            startDateTime = LocalDateTime.ofInstant(this.startDateTime, ZoneOffset.UTC),
            endDateTime = LocalDateTime.ofInstant(this.endDateTime, ZoneOffset.UTC),
            status = RequestStatus.valueOf(this.status)
        )
}