package com.moyeobus.application.route.model

data class LocalRouteInfo(
    val routeId: Long,
    val departure: String,
    val destination: String,
    val operateDate: String,
    val departureTime: String,
    val destinationTime: String,
    val status: String
)
