package com.moyeobus.infra.persistence.address.repotiory

import com.moyeobus.application.address.dto.StationDto
import com.moyeobus.infra.persistence.address.entity.AddressEntity
import com.moyeobus.infra.persistence.address.entity.AreaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AddressJpaRepository : JpaRepository<AddressEntity, Long> {

    @Query(
        """
           SELECT new com.moyeobus.application.address.dto.StationDto(a.id, a.name, a.lat, a.lon, a.postCode)
           FROM AddressEntity a
           WHERE a.area = :area
        """
    )
    fun findByArea(area: AreaEntity) : List<StationDto>
}