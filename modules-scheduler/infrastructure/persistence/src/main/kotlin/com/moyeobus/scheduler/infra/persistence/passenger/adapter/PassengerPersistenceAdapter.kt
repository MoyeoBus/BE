package com.moyeobus.scheduler.infra.persistence.passenger.adapter

import com.moyeobus.scheduler.application.passenger.out.PassengerOutPort
import com.moyeobus.scheduler.domain.passenger.Passenger
import com.moyeobus.scheduler.infra.persistence.exception.NotFoundException
import com.moyeobus.scheduler.infra.persistence.passenger.mapper.PassengerMapper
import com.moyeobus.scheduler.infra.persistence.passenger.repository.PassengerJpaRepository
import org.springframework.stereotype.Component

@Component
class PassengerPersistenceAdapter(
    private val mapper: PassengerMapper,
    private val repo: PassengerJpaRepository,
) : PassengerOutPort {

    override fun findById(id: Long): Passenger {
        val res = repo.findById(id).orElseThrow(
            { NotFoundException("Passenger(id=$id)") }
        )
        return mapper.toDomain(res)
    }
}
