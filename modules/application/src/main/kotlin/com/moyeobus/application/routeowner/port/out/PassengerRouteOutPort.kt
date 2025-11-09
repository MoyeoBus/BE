package com.moyeobus.application.routeowner.port.out

import com.moyeobus.domain.routeowner.PassengerRoute

interface PassengerRouteOutPort {
    fun save(passengerRoute: PassengerRoute)
    fun findBy(query: RouteOwnerQuery) : RouteOwnerPage
}