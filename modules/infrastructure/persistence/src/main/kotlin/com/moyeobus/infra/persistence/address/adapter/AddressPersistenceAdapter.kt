package com.moyeobus.infra.persistence.address.adapter

import com.moyeobus.application.address.port.out.AddressOutPort
import com.moyeobus.domain.route.Address
import com.moyeobus.infra.persistence.address.entity.AddressEntity
import com.moyeobus.infra.persistence.address.repotiory.AddressJpaRepository
import org.springframework.stereotype.Component

@Component
class AddressPersistenceAdapter(
    private val repo: AddressJpaRepository
): AddressOutPort {
    override fun findAll(): List<Address> {
        val res = repo.findAll()
        return res.map { it.toDomain() }
    }

    private fun AddressEntity.toDomain() =
        Address(
            id = this.id,
            area = this.area.id!!,
            name = this.name,
            lat = this.lat,
            lon = this.lon,
            postCode = this.postCode
        )
}