package com.moyeobus.infra.persistence.address.mapper

import com.moyeobus.domain.route.Area
import com.moyeobus.infra.persistence.address.entity.AreaEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface AreaMapper {
    fun toDomain(entity: AreaEntity) : Area
    fun toEntity(domain: Area) : AreaEntity
}