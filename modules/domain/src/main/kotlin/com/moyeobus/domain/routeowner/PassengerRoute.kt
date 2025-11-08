package com.moyeobus.domain.routeowner

import com.moyeobus.domain.route.Route
import com.moyeobus.domain.user.Passenger

data class PassengerRoute(
    val id: Long? = null,

    val passenger: Passenger,

    val route: Route
)
