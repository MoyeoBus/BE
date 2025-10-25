package com.moyeobus.infra.persistence.route.adapter

import com.moyeobus.application.route.port.out.RouteOutPort
import com.moyeobus.domain.route.RequestStatus
import com.moyeobus.domain.route.Route
import com.moyeobus.domain.route.RouteRequest
import com.moyeobus.infra.persistence.route.entity.RouteEntity
import com.moyeobus.infra.persistence.route.entity.RouteRequestEntity
import com.moyeobus.infra.persistence.route.repository.RouteJpaRepository
import java.time.LocalDateTime
import java.time.ZoneOffset

class RoutePersistenceAdapter(
    private val repo: RouteJpaRepository
) : RouteOutPort{
    override fun save(route: Route) {
        repo.save(route)
    }

    private fun RouteEntity.toDomain() =
        Route(
            id = this.id,
            stops = this.routeComponents.map { it.toDomain() },
            operatorId = this.operatorId,
            localGovId = this.localGovId,
            routeDistance = this.routeDistance,
            routeTotalTime = this.routeTotalTime
        )
}