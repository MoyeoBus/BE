package com.moyeobus.infra.persistence.address.repotiory

import com.moyeobus.infra.persistence.address.entity.AreaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AreaJpaRepository : JpaRepository<AreaEntity, Long>{

    @Query(
        """
            SELECT EXISTS (
                SELECT 1
                FROM AreaEntity a
                WHERE a.parentSigunguId = :id
            )
        """
    )
    fun existsParentById(id: Long) : Boolean

    @Query(
        """
            SELECT a FROM AreaEntity a where a.parentSigunguId is null
        """
    )
    fun findAllParents() : List<AreaEntity>

    @Query(
        """
            SELECT a FROM AreaEntity a where a.parentSigunguId = :parentSigunguId
        """
    )
    fun findAllChildren(parentSigunguId: Long) : List<AreaEntity>
}