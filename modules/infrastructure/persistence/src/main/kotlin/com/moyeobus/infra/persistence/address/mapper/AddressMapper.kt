package com.moyeobus.infra.persistence.address.mapper

import com.moyeobus.domain.route.Address
import com.moyeobus.infra.persistence.address.entity.AddressEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface AddressMapper {
    fun toDomain(entity: AddressEntity) : Address
    fun toEntity(entity: Address) : AddressEntity
}