package com.moyeobus.infra.persistence.address.adapter

import com.moyeobus.application.address.dto.StationDto
import com.moyeobus.application.address.port.out.AddressOutPort
import com.moyeobus.domain.route.Address
import com.moyeobus.domain.route.Area
import com.moyeobus.infra.exception.NotFoundException
import com.moyeobus.infra.persistence.address.mapper.AddressMapper
import com.moyeobus.infra.persistence.address.mapper.AreaMapper
import com.moyeobus.infra.persistence.address.repotiory.AddressJpaRepository
import org.springframework.stereotype.Component

@Component
class AddressPersistenceAdapter(
    private val repo: AddressJpaRepository,
    private val mapper: AddressMapper,
    private val areaMapper: AreaMapper
): AddressOutPort {
    override fun checkExists(id: Long): Boolean {
        return repo.existsById(id)
    }

    override fun findAll(): List<Address> {
        val res = repo.findAll()
        return res.map { mapper.toDomain(it) }
    }

    override fun findById(id: Long): Address {
        val res = repo.findById(id).orElseThrow({ NotFoundException("Address(id=$id)")})
        return mapper.toDomain(res)
    }

    override fun findByArea(area: Area): List<StationDto> {
        val res = repo.findByArea(areaMapper.toEntity(area))
        return res
    }

    override fun findAllByArea(areas: List<Area>): List<Address> {
        val areaIds = areas.map { it.id!! }
        val res = repo.findAllByAreaIdIn(areaIds)
        return res.map { mapper.toDomain(it)}
    }
}