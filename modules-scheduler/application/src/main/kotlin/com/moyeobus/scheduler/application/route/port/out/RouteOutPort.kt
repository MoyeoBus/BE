package com.moyeobus.scheduler.application.route.port.out

import com.moyeobus.scheduler.domain.route.Route


interface RouteOutPort {
    fun save(route: Route): Route
}