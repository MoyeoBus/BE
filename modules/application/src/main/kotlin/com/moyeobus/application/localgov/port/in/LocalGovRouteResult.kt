package com.moyeobus.application.localgov.port.`in`

data class LocalGovRouteResult(
    val govName: String,
    val items: List<LocalGovRouteWrapper>
)
