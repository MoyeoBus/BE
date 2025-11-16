package com.moyeobus.infra.persistence.route.repository

import com.moyeobus.domain.route.GeoPoint
import com.moyeobus.infra.persistence.route.dto.RouteDetailProjection
import com.moyeobus.infra.persistence.route.dto.RouteInfoProjection
import com.moyeobus.infra.persistence.route.dto.RouteTimeProjection
import com.moyeobus.infra.persistence.route.dto.TrackInfoProjection
import com.moyeobus.infra.persistence.route.dto.TrackItemProjection
import com.moyeobus.infra.persistence.route.entity.RouteComponentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant

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
    """
    )
    fun findAllByRouteIdIn(@Param("routeIds") routeIds: List<Long>): List<RouteComponentEntity>


    @Query("""
    SELECT 
        function('date', min(rc.assignedTime)) AS date,
        function('date_format', min(rc.assignedTime), '%H:%i') AS departureTime,
        function('date_format', max(rc.assignedTime), '%H:%i') AS destinationTime
    FROM RouteComponentEntity rc
    WHERE rc.routeId = :routeId
    """
    )
    fun findTimeRange(@Param("routeId") routeId: Long): RouteTimeProjection

    @Query(
        """
            SELECT 
                ROW_NUMBER() OVER (ORDER BY rc.assignedTime ASC, rc.id ASC) AS order,
                rc.name AS station,
                function('date_format', rc.assignedTime, '%H:%i') AS time
            FROM RouteComponentEntity rc
            WHERE rc.routeId = :routeId
            ORDER BY rc.id ASC
            """
    )
    fun findAllByRouteId(@Param("routeId") routeId: Long): List<RouteDetailProjection>


    @Query("""
    SELECT rc.assignedTime
    FROM RouteComponentEntity rc
    WHERE rc.routeId = :routeId 
      AND rc.name IN :names
    """)
    fun findTimeByLocationAndRoute(
        @Param("routeId") routeId: Long,
        @Param("names") names: List<String>
    ): List<Instant>


    @Query(
        value = """
        WITH current AS (
            SELECT id
            FROM route_component
            WHERE route_id = :routeId
              AND is_requested = 1
              AND name = :currentStation
            LIMIT 1
        ),
        next AS (
            SELECT MIN(id) AS id
            FROM route_component
            WHERE route_id = :routeId
              AND is_requested = 1
              AND id > (SELECT id FROM current)
        )
        
        SELECT 
            :routeId AS routeId,
            (SELECT name FROM route_component WHERE id = (SELECT id FROM next)) AS nextStation,
            (
                SELECT SUM(duration)
                FROM route_component
                WHERE route_id = :routeId
                  AND id > (SELECT id FROM current)
                  AND id <= (SELECT id FROM next)
            ) AS gapTime,
            (
                SELECT SUM(distance)
                FROM route_component
                WHERE route_id = :routeId
                  AND id > (SELECT id FROM current)
                  AND id <= (SELECT id FROM next)
            ) AS remainDistance
    """,
        nativeQuery = true
    )
    fun findRouteTrackInfo(
        @Param("routeId") routeId: Long,
        @Param("currentStation") currentStation: String
    ): TrackInfoProjection

    @Query(
        value = """
        SELECT 
            rc.name AS station,
            DATE_FORMAT(rc.assigned_time, '%H:%i') AS time,
            CASE
                WHEN rc.id = (
                    SELECT MIN(id)
                    FROM route_component
                    WHERE route_id = :routeId AND is_requested = 1
                ) THEN '출발'
                
                WHEN rc.id = (
                    SELECT MAX(id)
                    FROM route_component
                    WHERE route_id = :routeId AND is_requested = 1
                ) THEN '종점'
                
                WHEN rc.assigned_time < NOW() THEN '통과'
                
                ELSE '예정'
            END AS tag
        FROM route_component rc
        WHERE rc.route_id = :routeId AND rc.is_requested = 1
        ORDER BY rc.assigned_time ASC
    """,
        nativeQuery = true
    )
    fun findTrackItems(@Param("routeId") routeId: Long): List<TrackItemProjection>

    @Query("""
        SELECT rc.lat, rc.lon
        FROM RouteComponentEntity rc
        where routeId = :routeId
    """)
    fun findTrackPoints(@Param("routeId") routeId: Long): List<GeoPoint>

    @Query(
            """
            SELECT COUNT(*) 
            FROM route_component 
            WHERE route_id = :id
        """,
        nativeQuery = true
    )
    fun countComponents(@Param("id") id: Long): Int

}