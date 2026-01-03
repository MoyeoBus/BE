package com.moyeobus.infra.persistence.passenger.repository

import com.moyeobus.infra.persistence.passenger.entity.PassengerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PassengerJpaRepository : JpaRepository<PassengerEntity, Long> {
    @Query("""
        SELECT p FROM PassengerEntity p
        WHERE p.email = :email
    """)
    fun findByEmail(email: String): PassengerEntity?

    fun existsByEmail(email: String): Boolean
}