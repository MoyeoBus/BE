package com.moyeobus.infra.persistence.address.adapter

import com.moyeobus.application.address.port.out.AreaOutPort
import com.moyeobus.domain.route.Area
import com.moyeobus.infra.persistence.address.entity.AreaEntity
import com.moyeobus.infra.persistence.address.repotiory.AreaJpaRepository
import org.springframework.stereotype.Component

@Component
class AreaPersistenceAdapter(
) : AreaOutPort {

    private fun Area.toEntity() =
        AreaEntity(
            id = this.id,
            sigunguName = this.sigunguName,
            parentSigunguId = this.parentSigunguId
        )

    private fun Area.toDomain() =
        Area(
            id = this.id,
            sigunguName = this.sigunguName,
            parentSigunguId = this.parentSigunguId
        )

}