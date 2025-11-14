package com.moyeobus.api.operator.dto

import com.moyeobus.application.route.model.RouteDistanceRanking

data class RouteDistanceRankingResult(
    val items: List<RouteDistanceRanking>
)
