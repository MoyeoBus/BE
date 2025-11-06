package com.moyeobus.infra.persistence.address.adapter

import com.moyeobus.application.address.port.out.AddressOutPort
import com.moyeobus.domain.route.Address
import com.moyeobus.infra.exception.NotFoundException
import com.moyeobus.infra.persistence.address.mapper.AddressMapper
import com.moyeobus.infra.persistence.address.repotiory.AddressJpaRepository
import org.springframework.stereotype.Component

@Component
class AddressPersistenceAdapter(
    private val repo: AddressJpaRepository,
    private val mapper: AddressMapper
): AddressOutPort {
    override fun checkExists(id: Long): Boolean {
        return repo.existsById(id)
    }

    override fun findAll(): List<Address> {
        val res = repo.findAll()
        return res.map { mapper.toDomain(it) }
    }

    override fun findById(id: Long): Address {
        val res = repo.findById(id).
            orElseThrow { NotFoundException("Address(id=$id)") }
        return mapper.toDomain(res)
    }
}