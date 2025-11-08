package com.moyeobus.application.route.port.out

import com.moyeobus.domain.route.RouteRequest

data class RouteRequestCluster(
    val acceptedRequests: List<RouteRequest>,
    val exceptedRequests: List<RouteRequest>
)