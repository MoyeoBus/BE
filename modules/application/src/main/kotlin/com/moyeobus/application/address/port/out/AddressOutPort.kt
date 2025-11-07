package com.moyeobus.application.address.port.out

import com.moyeobus.application.address.dto.SpotDto
import com.moyeobus.domain.route.Address
import com.moyeobus.domain.route.Area

interface AddressOutPort {
    fun checkExists(id: Long) : Boolean
    fun findAll() : List<Address>
    fun findByArea(area: Area) : List<SpotDto>
}