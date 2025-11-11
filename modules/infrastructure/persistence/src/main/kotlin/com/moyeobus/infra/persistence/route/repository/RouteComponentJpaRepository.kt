package com.moyeobus.infra.persistence.route.repository

import com.moyeobus.infra.persistence.route.dto.RouteInfoProjection
import com.moyeobus.infra.persistence.route.entity.RouteComponentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RouteComponentJpaRepository : JpaRepository<RouteComponentEntity, Long> {
    @Query(
        value = """
            SELECT 
                MIN(rc.route_id) AS routeId,
                (SELECT rc1.name 
                 FROM route_component rc1 
                 WHERE rc1.route_id = :routeId
                 ORDER BY rc1.assigned_time ASC 
                 LIMIT 1) AS departure,
                (SELECT rc2.name 
                 FROM route_component rc2 
                 WHERE rc2.route_id = :routeId 
                 ORDER BY rc2.assigned_time DESC 
                 LIMIT 1) AS destination
            FROM route_component rc
            WHERE rc.route_id = :routeId 
        """,
        nativeQuery = true
    )
    fun findRouteEndpoints(@Param("routeId") routeId: Long): RouteInfoProjection

    @Query("""
        select rc from RouteComponentEntity rc
        where rc.routeId in :routeIds
        order by rc.routeId, rc.assignedTime asc
    """)
    fun findAllByRouteIdIn(@Param("routeIds") routeIds: List<Long>): List<RouteComponentEntity>

}