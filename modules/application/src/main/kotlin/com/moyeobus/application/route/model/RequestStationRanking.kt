package com.moyeobus.application.route.model

data class RequestStationRanking (
    val stationName: String,
    val ranking: Int,
    val requestCount: Long
)