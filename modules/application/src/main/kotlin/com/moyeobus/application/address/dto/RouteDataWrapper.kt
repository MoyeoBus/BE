package com.moyeobus.application.address.dto

import com.moyeobus.domain.route.Address

data class RouteDataWrapper(
    val stops: List<Address>,
    val routeDistance: Double,
    val routeTotalTime: Double
)
