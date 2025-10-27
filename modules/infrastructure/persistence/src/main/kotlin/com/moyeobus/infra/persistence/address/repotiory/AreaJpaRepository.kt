package com.moyeobus.infra.persistence.address.repotiory

import com.moyeobus.infra.persistence.address.entity.AreaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AreaJpaRepository : JpaRepository<AreaEntity, Long>{
}