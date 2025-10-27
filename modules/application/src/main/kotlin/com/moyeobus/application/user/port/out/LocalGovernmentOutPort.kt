package com.moyeobus.application.user.port.out

import com.moyeobus.domain.user.LocalGovernment

interface LocalGovernmentOutPort {
    fun findById(id: Long) : LocalGovernment
}