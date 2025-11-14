package com.moyeobus.application.route.model

data class RequestAreaRanking(
    val areaName: String,
    val ranking: Int,
    val requestCount: Long
)
