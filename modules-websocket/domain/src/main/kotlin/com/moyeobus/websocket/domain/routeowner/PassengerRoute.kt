package com.moyeobus.websocket.domain.routeowner

import com.moyeobus.websocket.domain.passenger.Passenger
import com.moyeobus.websocket.domain.route.Route


data class PassengerRoute(
    val id: Long? = null,

    val passenger: Passenger,

    val route: Route
)
