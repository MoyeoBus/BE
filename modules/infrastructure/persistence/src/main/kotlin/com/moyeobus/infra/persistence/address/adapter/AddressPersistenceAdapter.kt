package com.moyeobus.infra.persistence.address.adapter

import com.moyeobus.application.address.port.out.AddressOutPort
import com.moyeobus.domain.route.Address
import com.moyeobus.infra.persistence.address.mapper.AddressMapper
import com.moyeobus.infra.persistence.address.repotiory.AddressJpaRepository
import org.springframework.stereotype.Component

@Component
class AddressPersistenceAdapter(
    private val repo: AddressJpaRepository,
    private val mapper: AddressMapper
): AddressOutPort {
    override fun findAll(): List<Address> {
        val res = repo.findAll()
        return res.map { mapper.toDomain(it) }
    }
}