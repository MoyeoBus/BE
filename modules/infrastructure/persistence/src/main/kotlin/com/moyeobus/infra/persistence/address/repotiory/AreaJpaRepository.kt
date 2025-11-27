package com.moyeobus.infra.persistence.address.repotiory

import com.moyeobus.infra.persistence.address.entity.AreaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
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

    @Query("""
        SELECT a.id From AreaEntity a where a.sigunguName = :dosi
    """)
    fun findDosiId(@Param("dosi") dosi: String) : Long?

    @Query("""
        SELECT a From AreaEntity a 
        where a.parentSigunguId = :dosiId
        and a.sigunguName = :sigungu
    """)
    fun findSigunguByDosi(@Param("dosiId") dosiId: Long,
                          @Param("sigungu") sigungu: String) : AreaEntity?

    @Query(
        """
            SELECT a FROM AreaEntity a where a.parentSigunguId = :parentSigunguId
        """
    )
    fun findAllChildren(parentSigunguId: Long) : List<AreaEntity>
}