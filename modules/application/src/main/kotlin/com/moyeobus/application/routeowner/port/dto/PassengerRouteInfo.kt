package com.moyeobus.application.routeowner.port.dto

data class PassengerRouteInfo(
    val routeId: Long,
    val departure: String,
    val destination: String,
    val operatedDate: String,
    val assignedTime: List<String>,
    val status: String
)