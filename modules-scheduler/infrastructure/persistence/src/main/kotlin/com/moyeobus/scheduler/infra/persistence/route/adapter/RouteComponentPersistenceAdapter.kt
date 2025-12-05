package com.moyeobus.scheduler.infra.persistence.route.adapter

import com.moyeobus.scheduler.application.route.port.out.RouteComponentOutPort
import com.moyeobus.scheduler.domain.route.GeoPoint
import com.moyeobus.scheduler.domain.route.RouteComponent
import com.moyeobus.scheduler.infra.persistence.route.entity.RouteComponentEntity
import com.moyeobus.scheduler.infra.persistence.route.repository.RouteComponentJpaRepository
import org.springframework.stereotype.Component

import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class RouteComponentPersistenceAdapter(
    private val repo: RouteComponentJpaRepository
) : RouteComponentOutPort {

    override fun save(component: RouteComponent) {
        val entity = component.toEntity()
        repo.save(entity)
    }

    override fun saveAll(components: List<RouteComponent>) {
        repo.saveAll(components.map { it.toEntity() })
    }



    private fun RouteComponent.toEntity(): RouteComponentEntity =
        RouteComponentEntity(
            id = this.id,
            routeId = this.routeId,
            name = this.name,
            lat = this.location.lat,
            lon = this.location.lon,
            assignedTime = this.assignedTime.toInstant(ZoneOffset.UTC),
            distance = this.distance,
            duration = this.duration,
            isRequested = this.isRequested
        )


    private fun RouteComponentEntity.toDomain(): RouteComponent =
        RouteComponent(
            id = this.id,
            routeId = this.routeId,
            name = this.name,
            location = GeoPoint(lat = this.lat, lon = this.lon),
            assignedTime = LocalDateTime.ofInstant(this.assignedTime, ZoneOffset.UTC),
            distance = this.distance,
            duration = this.duration,
            isRequested = this.isRequested
        )

}