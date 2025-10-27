package com.moyeobus.application.route.port.out

import com.moyeobus.domain.route.Route

interface RouteOutPort {
    fun save(route: Route) : Route
}