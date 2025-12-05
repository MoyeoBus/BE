package com.moyeobus.scheduler.infra.persistence.routeowner.adapter

import com.moyeobus.scheduler.application.routeowner.port.out.PassengerRouteOutPort
import com.moyeobus.scheduler.domain.routeowner.PassengerRoute
import com.moyeobus.scheduler.infra.persistence.passenger.mapper.PassengerMapper
import com.moyeobus.scheduler.infra.persistence.route.adapter.RoutePersistenceAdapter
import com.moyeobus.scheduler.infra.persistence.routeowner.entity.PassengerRouteEntity
import com.moyeobus.scheduler.infra.persistence.routeowner.repository.PassengerRouteJpaRepository
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