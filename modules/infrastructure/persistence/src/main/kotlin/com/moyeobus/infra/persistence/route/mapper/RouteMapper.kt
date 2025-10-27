package com.moyeobus.infra.persistence.route.mapper

import com.moyeobus.domain.route.Route
import com.moyeobus.infra.persistence.route.entity.RouteEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface RouteMapper {
    fun toDomain(entity: RouteEntity) : Route
    fun toEntity(domain: Route) : RouteEntity
}