package com.moyeobus.application.bus.port.out

import com.moyeobus.domain.bus.Bus

interface BusOutPort {
    fun save(bus: Bus)
    fun findNumberById(id: Long) : Long
    fun findIdleBusesByOperatorId(operatorId: Long): List<Bus>
}