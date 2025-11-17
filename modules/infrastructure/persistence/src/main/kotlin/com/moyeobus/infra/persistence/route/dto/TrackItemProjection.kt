package com.moyeobus.infra.persistence.route.dto

interface TrackItemProjection {
    val station: String
    val time: String
    val tag: String
}