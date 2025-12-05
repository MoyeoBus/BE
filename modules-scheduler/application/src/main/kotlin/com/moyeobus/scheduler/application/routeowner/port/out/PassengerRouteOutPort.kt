package com.moyeobus.scheduler.application.routeowner.port.out

import com.moyeobus.scheduler.domain.routeowner.PassengerRoute

interface PassengerRouteOutPort {
    fun save(passengerRoute: PassengerRoute)
}