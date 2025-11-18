package com.moyeobus.application.route.model

import com.moyeobus.domain.route.GeoPoint

data class TrackItemOutput(
    val station: String,
    val geoPoint: GeoPoint,
    val time: String,
    val tag: String
)
