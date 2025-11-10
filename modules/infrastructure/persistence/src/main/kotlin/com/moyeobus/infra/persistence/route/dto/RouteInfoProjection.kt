package com.moyeobus.infra.persistence.route.dto


interface RouteInfoProjection {
    val routeId: Long
    val departure: String
    val destination: String
}
