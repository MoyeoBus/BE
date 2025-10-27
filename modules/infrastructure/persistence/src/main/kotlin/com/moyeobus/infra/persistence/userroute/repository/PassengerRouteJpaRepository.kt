package com.moyeobus.infra.persistence.userroute.repository

import com.moyeobus.infra.persistence.userroute.entity.PassengerRouteEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PassengerRouteJpaRepository : JpaRepository<PassengerRouteEntity, Long>{
}