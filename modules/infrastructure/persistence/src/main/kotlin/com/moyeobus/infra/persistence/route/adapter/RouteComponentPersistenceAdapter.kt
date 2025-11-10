package com.moyeobus.infra.persistence.route.adapter

import com.moyeobus.application.route.port.out.RouteComponentOutPort
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
    private val routeAdapter: RoutePersistenceAdapter  // Mapper 대신 Adapter 주입
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
            route = this.route?.let { routeAdapter.toEntity(it) },
            name = this.name,
            lat = this.location.lat,
            lon = this.location.lon,
            assignedTime = this.assignedTime.toInstant(ZoneOffset.UTC)
        )


    private fun RouteComponentEntity.toDomain(): RouteComponent =
        RouteComponent(
            id = this.id,
            route = null,
            name = this.name,
            location = GeoPoint(lat = this.lat, lon = this.lon),
            assignedTime = LocalDateTime.ofInstant(this.assignedTime, ZoneOffset.UTC)
        )

}