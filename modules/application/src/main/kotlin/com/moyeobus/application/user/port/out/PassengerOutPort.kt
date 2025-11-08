package com.moyeobus.application.user.port.out

import com.moyeobus.domain.user.Passenger

interface PassengerOutPort {
    fun findById(id: Long) : Passenger
}