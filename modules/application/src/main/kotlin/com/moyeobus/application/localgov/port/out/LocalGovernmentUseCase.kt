package com.moyeobus.application.localgov.port.out

import com.moyeobus.domain.route.RouteRequest

interface LocalGovernmentUseCase {
    fun queryLocal(id: Long) : Any
}