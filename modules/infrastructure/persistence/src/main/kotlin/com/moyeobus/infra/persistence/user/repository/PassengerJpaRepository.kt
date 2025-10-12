package com.moyeobus.infra.persistence.user.repository

import com.moyeobus.infra.persistence.user.entity.PassengerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PassengerJpaRepository : JpaRepository<PassengerEntity, Long>