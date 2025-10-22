package com.moyeobus.domain.route

data class Route (
    val stops : List<Address>,

    val routeDistance: Double,

    val routeTotalTime: Double
)