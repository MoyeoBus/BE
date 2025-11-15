package com.moyeobus.infra.persistence.route.dto

interface RouteTimeProjection {
    val date: String
    val departureTime: String
    val destinationTime: String
}