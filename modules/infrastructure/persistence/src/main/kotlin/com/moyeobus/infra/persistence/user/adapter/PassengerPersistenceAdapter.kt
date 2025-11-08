package com.moyeobus.infra.persistence.user.adapter

import com.moyeobus.application.user.port.out.PassengerOutPort
import com.moyeobus.domain.user.Passenger
import com.moyeobus.infra.exception.NotFoundException
import com.moyeobus.infra.persistence.user.mapper.PassengerMapper
import com.moyeobus.infra.persistence.user.repository.PassengerJpaRepository
import org.springframework.stereotype.Component

@Component
class PassengerPersistenceAdapter(
    private val mapper: PassengerMapper,
    private val repo: PassengerJpaRepository
) : PassengerOutPort {
    override fun findById(id: Long): Passenger {
        val res = repo.findById(id).orElseThrow(
            { NotFoundException("Passenger(id=$id)")}
        )
        return mapper.toDomain(res)
    }
}