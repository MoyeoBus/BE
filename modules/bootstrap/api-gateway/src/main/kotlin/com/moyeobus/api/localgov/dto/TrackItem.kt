package com.moyeobus.api.localgov.dto

import com.moyeobus.domain.route.GeoPoint

data class TrackItem(
    val station: String,
    val geoPoint: GeoPoint,
    val time: String,
    val tag: String
)
