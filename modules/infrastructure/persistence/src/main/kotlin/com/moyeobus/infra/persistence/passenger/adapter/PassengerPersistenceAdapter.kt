package com.moyeobus.infra.persistence.passenger.adapter

import com.moyeobus.application.passenger.port.out.PassengerOutPort
import com.moyeobus.domain.user.Passenger
import com.moyeobus.infra.exception.NotFoundException
import com.moyeobus.infra.persistence.passenger.repository.PassengerJpaRepository
import com.moyeobus.infra.persistence.passenger.mapper.PassengerMapper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class PassengerPersistenceAdapter(
    private val mapper: PassengerMapper,
    private val repo: PassengerJpaRepository,
    private val encoder: PasswordEncoder
) : PassengerOutPort {
    override fun existsByEmail(email: String): Boolean {
        return repo.existsByEmail(email)
    }

    override fun findById(id: Long): Passenger {
        val res = repo.findById(id).orElseThrow(
            { NotFoundException("Passenger(id=$id)") }
        )
        return mapper.toDomain(res)
    }

    override fun findByEmail(email: String): Passenger {
        val res = repo.findByEmail(email) ?: throw NotFoundException("Passenger(email=$email)")
        return mapper.toDomain(res)
    }

    override fun save(new: Passenger) {
        repo.save(mapper.toEntity(new))
    }

    override fun matches(raw: String, encoded: String): Boolean {
        return encoder.matches(raw, encoded)
    }

    override fun encode(raw: String): String {
        return encoder.encode(raw)
    }
}