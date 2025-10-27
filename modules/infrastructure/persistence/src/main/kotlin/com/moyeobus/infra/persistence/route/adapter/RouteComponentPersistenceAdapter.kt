package com.moyeobus.infra.persistence.route.adapter

import com.moyeobus.application.route.port.out.RouteComponentOutPort
import com.moyeobus.domain.route.Address
import com.moyeobus.domain.route.RouteComponent
import com.moyeobus.infra.persistence.address.mapper.AddressMapper
import com.moyeobus.infra.persistence.route.entity.RouteComponentEntity
import com.moyeobus.infra.persistence.route.mapper.RouteComponentMapper
import com.moyeobus.infra.persistence.route.mapper.RouteMapper
import com.moyeobus.infra.persistence.route.repository.RouteComponentJpaRepository
import org.springframework.stereotype.Component

@Component
class RouteComponentPersistenceAdapter(
    private val repo: RouteComponentJpaRepository,
    private val mapper: RouteComponentMapper
) : RouteComponentOutPort {
    override fun save(component: RouteComponent) {
        val entity = mapper.toEntity(component)
        repo.save(entity)
    }
}