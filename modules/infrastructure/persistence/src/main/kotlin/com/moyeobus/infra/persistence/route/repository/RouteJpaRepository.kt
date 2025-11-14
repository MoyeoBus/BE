package com.moyeobus.infra.persistence.route.repository

import com.moyeobus.infra.persistence.route.entity.RouteEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

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
}