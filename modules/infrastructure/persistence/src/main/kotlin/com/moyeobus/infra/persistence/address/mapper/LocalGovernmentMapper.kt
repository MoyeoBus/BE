package com.moyeobus.infra.persistence.address.mapper

import com.moyeobus.domain.user.LocalGovernment
import com.moyeobus.infra.persistence.user.entity.LocalGovernmentEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring", uses = [AreaMapper::class])
interface LocalGovernmentMapper {
    fun toDomain(entity: LocalGovernmentEntity) : LocalGovernment
    fun toEntity(domain: LocalGovernment) : LocalGovernmentEntity
}