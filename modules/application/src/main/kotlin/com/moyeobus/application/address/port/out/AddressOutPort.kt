package com.moyeobus.application.address.port.out

import com.moyeobus.domain.route.Address

interface AddressOutPort {
    fun checkExists(id: Long) : Boolean
    fun findAll() : List<Address>
    fun findById(id: Long) : Address
}