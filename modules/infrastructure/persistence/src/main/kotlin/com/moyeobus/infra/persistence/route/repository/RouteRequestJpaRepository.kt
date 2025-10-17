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
}