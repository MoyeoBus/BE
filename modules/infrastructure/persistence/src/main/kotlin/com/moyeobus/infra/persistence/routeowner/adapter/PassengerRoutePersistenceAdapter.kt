package com.moyeobus.infra.persistence.routeowner.adapter

import com.moyeobus.application.routeowner.port.dto.PassengerRouteDto
import com.moyeobus.application.routeowner.port.out.PassengerRouteOutPort
import com.moyeobus.application.routeowner.port.out.RouteOwnerPage
import com.moyeobus.application.routeowner.port.out.RouteOwnerQuery
import com.moyeobus.domain.routeowner.PassengerRoute
import com.moyeobus.infra.persistence.route.adapter.RoutePersistenceAdapter
import com.moyeobus.infra.persistence.routeowner.dto.PassengerRouteEntityDto
import com.moyeobus.infra.persistence.routeowner.entity.PassengerRouteEntity
import com.moyeobus.infra.persistence.routeowner.repository.PassengerRouteJpaRepository
import com.moyeobus.infra.persistence.passenger.mapper.PassengerMapper
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.time.ZoneOffset

@Component
class PassengerRoutePersistenceAdapter(
    private val mapper: PassengerMapper,
    private val repo: PassengerRouteJpaRepository,
    private val adapter: RoutePersistenceAdapter
) : PassengerRouteOutPort {

    override fun save(passengerRoute: PassengerRoute) {
        repo.save(toEntity(passengerRoute))
    }

    override fun findBy(query: RouteOwnerQuery): RouteOwnerPage {
        val pageSize = query.limit
        val list = repo.pageBy(
            passengerId = query.passengerId,
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

        return RouteOwnerPage(
            items = items.map { toDomainDto(it) },
            hasNext = hasNext,
            nextCursorCreatedAt = last?.createdAt?.let { java.time.LocalDateTime.ofInstant(it, ZoneOffset.UTC) },
            nextCursorId = last?.id,
        )
    }

    override fun countByLocal(id: Long): Int {
        return repo.countPassengerByLocal(id)
    }

    fun toDomain(entity: PassengerRouteEntity): PassengerRoute =
        PassengerRoute(
            id = entity.id,
            passenger = mapper.toDomain(entity.passengerEntity),
            route = adapter.toDomain(entity.route)
    )
    fun toEntity(domain: PassengerRoute): PassengerRouteEntity =
        PassengerRouteEntity(
            id = null,
            passengerEntity = mapper.toEntity(domain.passenger),
            route = adapter.toEntity(domain.route)
    )
    fun toDomainDto(entityDto: PassengerRouteEntityDto): PassengerRouteDto =
        PassengerRouteDto(
            id = entityDto.id,
            route = adapter.toDomain(entityDto.route),
            createdAt = entityDto.createdAt
        )

}