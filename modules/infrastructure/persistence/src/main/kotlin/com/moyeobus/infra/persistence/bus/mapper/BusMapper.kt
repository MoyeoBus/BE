package com.moyeobus.infra.persistence.bus.mapper

import com.moyeobus.domain.bus.Bus
import com.moyeobus.infra.persistence.bus.entity.BusEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface BusMapper {
    fun toDomain(entity: BusEntity) : Bus
    fun toEntity(domain: Bus) : BusEntity
}