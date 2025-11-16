package com.moyeobus.infra.persistence.route.dto

interface TrackInfoProjection {
    val routeId: Long
    val nextStation: String
    val gapTime: Int
    val remainDistance: Int
}