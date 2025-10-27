package com.moyeobus.infra.persistence.userroute.adapter

import com.moyeobus.application.userroute.port.out.PassengerRouteOutPort
import com.moyeobus.infra.persistence.userroute.repository.PassengerRouteJpaRepository

class PassengerRoutePersistenceAdapter(
    private val repo: PassengerRouteJpaRepository
) : PassengerRouteOutPort {
}