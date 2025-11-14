package com.moyeobus.application.transport.port.out

import com.moyeobus.domain.user.TransportOperator

interface TransportOperatorOutPort {
    fun findById(id: Long) : TransportOperator
}