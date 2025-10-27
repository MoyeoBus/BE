package com.moyeobus.infra.persistence.route.mapper

import com.moyeobus.domain.route.RouteComponent
import com.moyeobus.infra.persistence.route.entity.RouteComponentEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface RouteComponentMapper {
    fun toDomain(entity: RouteComponentEntity) : RouteComponent
    fun toEntity(domain: RouteComponent) : RouteComponentEntity
}