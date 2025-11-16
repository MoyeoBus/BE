package com.moyeobus.api.localgov.dto

import com.moyeobus.application.route.model.RouteTrackInfo
import com.moyeobus.domain.route.GeoPoint

data class RouteTrackResponse(
    val info: RouteTrackInfo,
    val items: List<TrackItem>,
    val points: List<GeoPoint>
)
