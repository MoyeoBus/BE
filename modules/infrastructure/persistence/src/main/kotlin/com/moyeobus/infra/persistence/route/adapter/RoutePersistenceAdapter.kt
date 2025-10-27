package com.moyeobus.infra.persistence.route.adapter

import com.moyeobus.application.route.port.out.RouteOutPort
import com.moyeobus.domain.route.Route
import com.moyeobus.infra.persistence.route.mapper.RouteMapper
import com.moyeobus.infra.persistence.route.repository.RouteJpaRepository
import org.springframework.stereotype.Component

@Component
class RoutePersistenceAdapter(
    private val repo: RouteJpaRepository,
    private val mapper: RouteMapper
) : RouteOutPort{
    override fun save(route: Route): Route {
        return mapper.toDomain(repo.save(mapper.toEntity(route)))
    }
}