package com.moyeobus.scheduler.infra.persistence.passenger.repository

import com.moyeobus.scheduler.infra.persistence.passenger.entity.PassengerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PassengerJpaRepository : JpaRepository<PassengerEntity, Long>{
}