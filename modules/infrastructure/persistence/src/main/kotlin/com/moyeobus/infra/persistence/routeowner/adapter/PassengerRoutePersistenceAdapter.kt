package com.moyeobus.infra.persistence.routeowner.adapter

import com.moyeobus.application.routeowner.port.out.PassengerRouteOutPort
import com.moyeobus.domain.routeowner.PassengerRoute
import com.moyeobus.infra.persistence.route.adapter.RoutePersistenceAdapter
import com.moyeobus.infra.persistence.routeowner.entity.PassengerRouteEntity
import com.moyeobus.infra.persistence.routeowner.repository.PassengerRouteJpaRepository
import com.moyeobus.infra.persistence.user.mapper.PassengerMapper
import org.springframework.stereotype.Component

@Component
class PassengerRoutePersistenceAdapter(
    private val mapper: PassengerMapper,
    private val repo: PassengerRouteJpaRepository,
    private val adapter: RoutePersistenceAdapter
) : PassengerRouteOutPort {

    override fun save(passengerRoute: PassengerRoute) {
        repo.save(toEntity(passengerRoute))
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
}