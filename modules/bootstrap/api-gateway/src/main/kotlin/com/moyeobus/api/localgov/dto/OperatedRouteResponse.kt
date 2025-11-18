package com.moyeobus.api.localgov.dto

import com.moyeobus.domain.route.GeoPoint

data class OperatedRouteResponse(
    val info: OperationResult,
    val items: List<TrackItem>,
    val points: List<GeoPoint>
)
