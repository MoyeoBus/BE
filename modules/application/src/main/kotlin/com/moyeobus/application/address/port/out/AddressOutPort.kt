package com.moyeobus.application.address.port.out

import com.moyeobus.domain.route.Address

interface AddressOutPort {
    fun findAll() : List<Address>
}