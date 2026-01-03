package com.moyeobus.application.passenger.service

import com.moyeobus.application.passenger.port.`in`.PassengerQueryUseCase
import com.moyeobus.application.passenger.port.out.PassengerOutPort
import com.moyeobus.domain.user.Passenger
import org.springframework.stereotype.Service

@Service
class PassengerQueryService(
    private val repo: PassengerOutPort
) : PassengerQueryUseCase {
    override fun queryByEmail(email: String): Passenger {
        return repo.findByEmail(email)
    }
}