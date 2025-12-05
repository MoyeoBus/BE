package com.moyeobus.scheduler.application.passenger.out

import com.moyeobus.scheduler.domain.passenger.Passenger

interface PassengerOutPort {
    fun findById(id: Long) : Passenger
}