package com.moyeobus.application.passenger.port.out

import com.moyeobus.domain.user.Passenger

interface PassengerOutPort {
    fun existsByEmail(email: String) : Boolean
    fun findById(id: Long) : Passenger
    fun findByEmail(email: String) : Passenger
    fun save(new: Passenger)
    fun matches(raw: String, encoded: String) : Boolean
    fun encode(raw: String) : String
}