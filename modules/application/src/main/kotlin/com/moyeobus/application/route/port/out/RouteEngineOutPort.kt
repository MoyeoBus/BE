package com.moyeobus.application.route.port.out

import com.moyeobus.domain.route.Address
import com.moyeobus.domain.route.Route
import com.moyeobus.domain.route.RouteComponent

interface RouteEngineOutPort {
    fun calculatePath(stops: List<Address>): Route
}