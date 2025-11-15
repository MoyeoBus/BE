package com.moyeobus.infra.persistence.route.dto


interface RouteDetailProjection {
    val order: Int
    val station: String
    val time: String
}