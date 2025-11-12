package com.moyeobus.application.localgov.port.out

import com.moyeobus.domain.user.LocalGovernment

interface LocalGovernmentOutPort {
    fun findById(id: Long) : LocalGovernment
}