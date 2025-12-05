package com.moyeobus.scheduler.infra.persistence.route.repository

import com.moyeobus.scheduler.infra.persistence.route.entity.RouteRequestEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RouteRequestJpaRepository : JpaRepository<RouteRequestEntity, Long> {

    @Query(
        """
    SELECT r 
    FROM RouteRequestEntity r 
    WHERE r.status = :status
    """
    )
    fun findByStatus(@Param("status") status: String): List<RouteRequestEntity>
}