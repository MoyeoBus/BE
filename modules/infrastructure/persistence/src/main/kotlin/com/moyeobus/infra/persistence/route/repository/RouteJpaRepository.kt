package com.moyeobus.infra.persistence.route.repository

import com.moyeobus.infra.persistence.route.entity.RouteEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface RouteJpaRepository : JpaRepository<RouteEntity, Long> {

    @Query("""
        SELECT r.status
        FROM RouteEntity r
        Where r.id = :id
    """)
    fun findStatus(id: Long) : String

    @Query("""
        SELECT r
        FROM RouteEntity r
        Where r.localGovId = :id
    """)
    fun findByLocal(id: Long) : List<RouteEntity>

    @Query("""
        SELECT r
        FROM RouteEntity r
        Where r.operatorId = :id
    """)
    fun findByOperator(id: Long) : List<RouteEntity>

    @Query(
        """
        select r
        from RouteEntity r
        where (r.localGovId = :localGovId)
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
    fun pageByLocal(
        @Param("localGovId") localGovId: Long?,
        @Param("status") status: String?,
        @Param("fromAt") fromAt: Instant?,
        @Param("toAt") toAt: Instant?,
        @Param("cursorCreatedAt") cursorCreatedAt: Instant?,
        @Param("cursorId") cursorId: Long?,
        org: org.springframework.data.domain.Pageable,
    ): List<RouteEntity>
}