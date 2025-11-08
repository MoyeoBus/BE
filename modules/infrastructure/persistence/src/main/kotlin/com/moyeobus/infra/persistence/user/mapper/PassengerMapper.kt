package com.moyeobus.infra.persistence.user.mapper

import com.moyeobus.domain.user.Passenger
import com.moyeobus.infra.persistence.user.entity.PassengerEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface PassengerMapper {
    fun toDomain(entity: PassengerEntity) : Passenger
    fun toEntity(domain: Passenger) : PassengerEntity
}