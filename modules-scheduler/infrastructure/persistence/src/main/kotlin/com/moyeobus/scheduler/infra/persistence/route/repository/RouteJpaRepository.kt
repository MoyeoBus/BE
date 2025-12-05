package com.moyeobus.scheduler.infra.persistence.route.repository

import com.moyeobus.scheduler.infra.persistence.route.entity.RouteEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RouteJpaRepository : JpaRepository<RouteEntity, Long> {

}