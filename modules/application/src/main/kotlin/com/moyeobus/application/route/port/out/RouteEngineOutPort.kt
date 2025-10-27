package com.moyeobus.application.route.port.out

import com.moyeobus.application.address.dto.RouteDataWrapper
import com.moyeobus.domain.route.Address

interface RouteEngineOutPort {
    fun calculatePath(stops: List<Address>): RouteDataWrapper
}