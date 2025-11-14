package com.moyeobus.infra.persistence.route.repository

import com.moyeobus.infra.persistence.route.dto.DateUseProjection
import com.moyeobus.infra.persistence.route.dto.HourUseProjection
import com.moyeobus.infra.persistence.route.dto.AddressRankProjection
import com.moyeobus.infra.persistence.route.entity.RouteRequestEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant
import java.time.LocalDate

@Repository
interface RouteRequestJpaRepository : JpaRepository<RouteRequestEntity, Long> {

    @Query(
        """
        SELECT 
            DATE(r.start_date_time) AS date,
            COUNT(*) AS useCount
        FROM route_request r
        WHERE 
            r.destination_id IN (:ids) or r.departure_id IN (:ids)
            AND YEAR(r.start_date_time) = YEAR(CURDATE())
            AND MONTH(r.start_date_time) = MONTH(CURDATE())
        GROUP BY DATE(r.start_date_time)
        ORDER BY DATE(r.start_date_time)
        """,
        nativeQuery = true
    )
    fun countMonthlyUse(
        @Param("ids") ids: List<Long>
    ): List<DateUseProjection>

    @Query(
        """
            SELECT 
                HOUR(r.start_date_time) AS hour,
                COUNT(*) AS useCount
            FROM route_request r
            WHERE 
                r.id IN (:requestIds)
                AND DATE(r.start_date_time) = :targetDate
            GROUP BY HOUR(r.start_date_time)
            ORDER BY HOUR(r.start_date_time)
            """,
        nativeQuery = true
    )
    fun countHourlyUse(
        @Param("requestIds") requestIds: List<Long>,
        @Param("targetDate") targetDate: LocalDate
    ): List<HourUseProjection>

    @Query(
        """
        select r from RouteRequestEntity r
        where (:passengerId is null or r.passengerId = :passengerId)
          and (:status is null or r.status = :status)
          and (:fromAt is null or r.createdAt >= :fromAt)
          and (:toAt is null or r.createdAt < :toAt)
          and (
                (:cursorCreatedAt is null and :cursorId is null)
             or (r.createdAt < :cursorCreatedAt)
             or (r.createdAt = :cursorCreatedAt and r.id < :cursorId)
          )
        order by r.createdAt desc, r.id desc
        """,
    )
    fun pageBy(
        @Param("passengerId") passengerId: Long?,
        @Param("status") status: String?,
        @Param("fromAt") fromAt: Instant?,
        @Param("toAt") toAt: Instant?,
        @Param("cursorCreatedAt") cursorCreatedAt: Instant?,
        @Param("cursorId") cursorId: Long?,
        org: org.springframework.data.domain.Pageable,
    ): List<RouteRequestEntity>

    @Query(
        """
        select 
        count(r) as totalCount,
        count(case when r.status = 'APPROVED' then 1 end) as approvedCount,
        count(case when r.status = 'CANCELLED' then 1 end) as cancelledCount,
        count(case when r.status = 'PENDING' then 1 end) as pendingCount
        from RouteRequestEntity r
        where (:passengerId is null or r.passengerId = :passengerId)
          and (:status is null or r.status = :status)
          and (:fromAt is null or r.createdAt >= :fromAt)
          and (:toAt is null or r.createdAt < :toAt)
        order by r.createdAt desc, r.id desc
        """,
    )
    fun summary(
        @Param("passengerId") passengerId: Long?,
        @Param("status") status: String?,
        @Param("fromAt") fromAt: Instant?,
        @Param("toAt") toAt: Instant?,
    ): List<Array<Any>>

    @Query(
        """
    SELECT r 
    FROM RouteRequestEntity r 
    WHERE r.status = :status
    """
    )
    fun findByStatus(@Param("status") status: String): List<RouteRequestEntity>

    @Query("""
    SELECT r 
    FROM RouteRequestEntity r 
    WHERE r.destination.id in :addressIds
    """
    )
    fun findByAddressIds(@Param("addressIds") addressIds: List<Long?>) : List<RouteRequestEntity>

    @Query("""
    SELECT r 
    FROM RouteRequestEntity r 
    WHERE r.routeId in :routeIds
    """
    )
    fun findByRouteIds(@Param("routeIds") routeIds: List<Long?>) : List<RouteRequestEntity>

    @Query("""
        SELECT r.departure AS address, count(r) AS requestCount
        FROM RouteRequestEntity r
        WHERE r.routeId in :routeIds
        GROUP BY r.departure.id
    """)
    fun findDepartureCountByRoute(@Param("routeIds") routeIds: List<Long?>) : List<AddressRankProjection>

    @Query("""
        SELECT r.destination AS address, count(r) AS requestCount
        FROM RouteRequestEntity r
        WHERE r.routeId in :routeIds
        GROUP BY r.destination.id
    """)
    fun findDestinationCountByRoute(@Param("routeIds") routeIds: List<Long?>) : List<AddressRankProjection>
}