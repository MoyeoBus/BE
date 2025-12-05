package com.moyeobus.scheduler.infra.persistence.routeowner.repository

import com.moyeobus.scheduler.infra.persistence.routeowner.entity.PassengerRouteEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PassengerRouteJpaRepository : JpaRepository<PassengerRouteEntity, Long>{

}