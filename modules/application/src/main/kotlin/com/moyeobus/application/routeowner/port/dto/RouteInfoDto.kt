package com.moyeobus.application.routeowner.port.dto

data class RouteInfoDto(
    val routeId: Long,
    val departure: String,
    val destination: String,
    val operateDate: String,
    val departureTime: String,
    val destinationTime: String,
    val status: String
)