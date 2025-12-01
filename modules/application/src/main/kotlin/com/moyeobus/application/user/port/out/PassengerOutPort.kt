package com.moyeobus.application.user.port.out

import com.moyeobus.domain.user.Passenger

interface PassengerOutPort {
    fun findById(id: Long) : Passenger
    fun findByEmail(email: String) : Passenger
    fun matches(raw: String, encoded: String) : Boolean
}