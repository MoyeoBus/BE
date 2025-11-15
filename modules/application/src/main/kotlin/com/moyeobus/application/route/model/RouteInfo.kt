package com.moyeobus.application.route.model

data class RouteInfo(
    val busNumber: Long,
    val departureName: String,
    val destinationName: String,
    val operateDate: String,
    val departTime: String,
    val arrivalTime: String,
)
