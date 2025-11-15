package com.moyeobus.infra.persistence.route.adapter

import com.moyeobus.application.route.model.RouteItem
import com.moyeobus.application.route.port.out.RouteComponentOutPort
import com.moyeobus.application.route.port.out.RouteInfoWrapper
import com.moyeobus.application.route.port.out.RouteTimeRange
import com.moyeobus.domain.route.GeoPoint
import com.moyeobus.domain.route.RouteComponent
import com.moyeobus.infra.persistence.route.entity.RouteComponentEntity
import com.moyeobus.infra.persistence.route.repository.RouteComponentJpaRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class RouteComponentPersistenceAdapter(
    private val repo: RouteComponentJpaRepository,
    private val routeAdapter: RoutePersistenceAdapter
) : RouteComponentOutPort {

    override fun save(component: RouteComponent) {
        val entity = component.toEntity()
        repo.save(entity)
    }

    override fun saveAll(components: List<RouteComponent>) {
        repo.saveAll(components.map { it.toEntity() })
    }

    override fun findById(id: Long) : RouteInfoWrapper {
        val res = repo.findRouteEndpoints(id)
        return RouteInfoWrapper(
            routeId = res.routeId,
            departure = res.departure,
            destination = res.destination
        )
    }

    override fun findAllByRouteIdIn(routeIds: List<Long>): List<RouteComponent> {
        val res = repo.findAllByRouteIdIn(routeIds)
        return res.map { it.toDomain() }
    }

    override fun findTimeRange(routeId: Long): RouteTimeRange {
        val res = repo.findTimeRange(routeId)
        return RouteTimeRange(res.date, res.departureTime, res.destinationTime)
    }

    override fun findAllByRouteId(routeId: Long): List<RouteItem> {
        val res = repo.findAllByRouteId(routeId)
        val items = res.map { RouteItem(
            order = it.order,
            station = it.station,
            time = it.time
        ) }
        return items
    }

    override fun countStations(routeId: Long): Int {
        return repo.countComponents(routeId)
    }

    private fun RouteComponent.toEntity(): RouteComponentEntity =
        RouteComponentEntity(
            id = this.id,
            routeId = this.routeId,
            name = this.name,
            lat = this.location.lat,
            lon = this.location.lon,
            assignedTime = this.assignedTime.toInstant(ZoneOffset.UTC)
        )


    private fun RouteComponentEntity.toDomain(): RouteComponent =
        RouteComponent(
            id = this.id,
            routeId = this.routeId,
            name = this.name,
            location = GeoPoint(lat = this.lat, lon = this.lon),
            assignedTime = LocalDateTime.ofInstant(this.assignedTime, ZoneOffset.UTC)
        )

}