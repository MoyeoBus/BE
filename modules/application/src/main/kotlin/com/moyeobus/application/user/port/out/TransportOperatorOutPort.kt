package com.moyeobus.application.user.port.out

import com.moyeobus.domain.user.TransportOperator

interface TransportOperatorOutPort {
    fun findById(id: Long) : TransportOperator
}