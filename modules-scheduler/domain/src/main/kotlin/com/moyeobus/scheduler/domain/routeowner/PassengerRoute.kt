package com.moyeobus.scheduler.domain.routeowner

import com.moyeobus.scheduler.domain.passenger.Passenger
import com.moyeobus.scheduler.domain.route.Route


data class PassengerRoute(
    val id: Long? = null,

    val passenger: Passenger,

    val route: Route
)
