package com.moyeobus.infra.persistence.route.repository

import com.moyeobus.infra.persistence.route.entity.RouteRequestEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface RouteRequestJpaRepository : JpaRepository<RouteRequestEntity, Long> {
    @Query(
        """
        select r from RouteRequestEntity r
        where r.passengerId = :passengerId
        order by r.createdAt desc, r.id desc
        """,
    )
    fun pageBy(
        @Param("passengerId") passengerId: Long
    ): List<RouteRequestEntity>
}