package com.moyeobus.infra.persistence.bus.adapter

import com.moyeobus.application.bus.port.out.BusOutPort
import com.moyeobus.domain.bus.Bus
import com.moyeobus.infra.persistence.bus.mapper.BusMapper
import com.moyeobus.infra.persistence.bus.repository.BusJpaRepository
import org.springframework.stereotype.Component

@Component
class BusPersistenceAdapter(
    private val repo: BusJpaRepository,
    private val mapper: BusMapper
) : BusOutPort {
    override fun save(bus: Bus) {
        repo.save(mapper.toEntity(bus))
    }

    override fun findIdleBusesByOperatorId(operatorId: Long): List<Bus> {
        val busEntities = repo.findIdleBusesByOperatorId(operatorId)
        return busEntities.map { mapper.toDomain(it) }
    }
}