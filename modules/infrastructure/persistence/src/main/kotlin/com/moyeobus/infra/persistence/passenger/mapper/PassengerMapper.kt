package com.moyeobus.infra.persistence.passenger.mapper

import com.moyeobus.domain.user.Passenger
import com.moyeobus.infra.persistence.passenger.entity.PassengerEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface PassengerMapper {
    fun toDomain(entity: PassengerEntity) : Passenger
    fun toEntity(domain: Passenger) : PassengerEntity
}