package com.moyeobus.infra.persistence.route.adapter


interface RouteTimeRangeProjection {
    val routeId: Long
    val departureStation: String
    val destinationStation: String
}