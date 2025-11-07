package com.moyeobus.application.address.port.out

import com.moyeobus.domain.route.Area

interface AreaOutPort {
    fun findById(id: Long): Area
}