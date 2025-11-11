package com.moyeobus.infra.persistence.routeowner.repository

import com.moyeobus.infra.persistence.routeowner.dto.PassengerRouteEntityDto
import com.moyeobus.infra.persistence.routeowner.entity.PassengerRouteEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface PassengerRouteJpaRepository : JpaRepository<PassengerRouteEntity, Long>{

    @Query(
        """
        select new com.moyeobus.infra.persistence.routeowner.dto.PassengerRouteEntityDto(r.id, r.route, r.createdAt)
        from PassengerRouteEntity r
        where (r.passengerEntity.id = :passengerId)
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
    ): List<PassengerRouteEntityDto>
}