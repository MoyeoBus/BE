package com.moyeobus.scheduler.infra.persistence.passenger.mapper

import com.moyeobus.scheduler.infra.persistence.passenger.entity.PassengerEntity
import com.moyeobus.scheduler.domain.passenger.Passenger
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface PassengerMapper {
    fun toDomain(entity: PassengerEntity) : Passenger
    fun toEntity(domain: Passenger) : PassengerEntity
}