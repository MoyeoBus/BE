package com.moyeobus.infra.persistence.route.dto

interface TrackItemProjection {
    val station: String
    val lat: Double
    val lon: Double
    val time: String
    val tag: String
}