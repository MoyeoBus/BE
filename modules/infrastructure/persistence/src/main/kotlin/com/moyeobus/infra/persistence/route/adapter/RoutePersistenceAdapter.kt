package com.moyeobus.infra.persistence.route.adapter

import com.moyeobus.application.route.port.out.RouteOutPort
import com.moyeobus.domain.route.Route
import com.moyeobus.domain.route.RouteStatus
import com.moyeobus.infra.persistence.route.entity.RouteEntity
import com.moyeobus.infra.persistence.route.repository.RouteJpaRepository
import org.springframework.stereotype.Component

@Component
class RoutePersistenceAdapter(
    private val repo: RouteJpaRepository
) : RouteOutPort {

    override fun save(route: Route): Route {
        val entity = toEntity(route)
        val saved = repo.save(entity)
        return toDomain(saved)
    }

    override fun findStatus(id: Long): String {
        return repo.findStatus(id)
    }

    override fun findByLocal(id: Long): List<Route> {
        val res = repo.findByLocal(id)
        return res.map { toDomain(it) }
    }

    override fun findByOperator(id: Long): List<Route> {
        val res = repo.findByOperator(id)
        return res.map { toDomain(it) }
    }

    fun toEntity(domain: Route): RouteEntity = RouteEntity(
        id = domain.id,
        operatorId = domain.operatorId,
        localGovId = domain.localGovId,
        busId = domain.busId ?: 0L,  // null 처리 필요
        routeDistance = domain.routeDistance.toDouble(),
        routeTotalTime = domain.routeTotalTime.toDouble(),
        status = domain.status.toString()
    )

    fun toDomain(entity: RouteEntity): Route = Route(
        id = entity.id,
        operatorId = entity.operatorId,
        localGovId = entity.localGovId,
        busId = entity.busId.takeIf { it > 0 },  // 0이면 null로
        routeDistance = entity.routeDistance.toInt(),
        routeTotalTime = entity.routeTotalTime.toInt(),
        status = RouteStatus.valueOf(entity.status)
    )
}