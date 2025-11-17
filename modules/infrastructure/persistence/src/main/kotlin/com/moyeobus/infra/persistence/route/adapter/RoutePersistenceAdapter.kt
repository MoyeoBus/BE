package com.moyeobus.infra.persistence.route.adapter

import com.moyeobus.application.route.model.BusUsageCount
import com.moyeobus.application.route.port.out.LocalRoutePage
import com.moyeobus.application.route.port.out.LocalRouteQuery
import com.moyeobus.application.route.port.out.RouteOutPort
import com.moyeobus.domain.route.Route
import com.moyeobus.domain.route.RouteStatus
import com.moyeobus.infra.exception.NotFoundException
import com.moyeobus.infra.persistence.route.entity.RouteEntity
import com.moyeobus.infra.persistence.route.repository.RouteJpaRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.time.ZoneOffset

@Component
class RoutePersistenceAdapter(
    private val repo: RouteJpaRepository
) : RouteOutPort {

    override fun save(route: Route): Route {
        val entity = toEntity(route)
        val saved = repo.save(entity)
        return toDomain(saved)
    }

    override fun findById(id: Long): Route {
        val res = repo.findById(id)
            .orElseThrow({ NotFoundException("Route(id=$id)") })
        return toDomain(res)
    }

    override fun findStatus(id: Long): String {
        return repo.findStatus(id)
    }

    override fun findByLocal(id: Long): List<Route> {
        val res = repo.findByLocal(id)
        return res.map { toDomain(it) }
    }

    override fun findNotCompletedByOperator(id: Long): List<Route> {
        val res = repo.findNotCompletedByOperator(id)
        return res.map { toDomain(it) }
    }


    override fun findBy(query: LocalRouteQuery): LocalRoutePage {
        val pageSize = query.limit
        val list = repo.pageByLocal(
            localGovId = query.localGovId,
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

        return LocalRoutePage(
            items = items.map { toDomain(it) },
            hasNext = hasNext,
            nextCursorCreatedAt = last?.createdAt?.let { java.time.LocalDateTime.ofInstant(it, ZoneOffset.UTC) },
            nextCursorId = last?.id,
        )
    }

    override fun findByOperator(id: Long): List<Route> {
        val res = repo.findByOperator(id)
        return res.map { toDomain(it) }
    }

    override fun countBusUsage(operatorId: Long): BusUsageCount {
        val res = repo.countBusUsage(operatorId)
        return BusUsageCount(res.operateCount, res.completedCount)
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