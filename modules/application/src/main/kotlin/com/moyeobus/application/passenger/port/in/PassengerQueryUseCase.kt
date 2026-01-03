package com.moyeobus.application.passenger.port.`in`

import com.moyeobus.domain.user.Passenger

interface PassengerQueryUseCase {
    fun queryByEmail(email: String) : Passenger
}