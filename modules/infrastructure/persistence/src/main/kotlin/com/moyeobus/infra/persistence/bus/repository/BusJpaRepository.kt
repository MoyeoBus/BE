package com.moyeobus.infra.persistence.bus.repository

import com.moyeobus.infra.persistence.bus.entity.BusEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BusJpaRepository : JpaRepository<BusEntity, Long> {

    @Query(
        """
            SELECT b FROM BusEntity b 
            WHERE b.operatorId = :operatorId AND b.isOperating = False
            """
    )
    fun findNotOperatingByOperatorId(operatorId: Long) : List<BusEntity>
}