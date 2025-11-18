package com.moyeobus.infra.persistence.route.dto


interface TrackInfoProjection {
    val routeId: Long
    val nextStation: String?
    val nextLat: Double?
    val nextLon: Double?
    val gapTime: Int?
    val remainDistance: Int?
}