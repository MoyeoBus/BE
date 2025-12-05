package com.moyeobus.scheduler.application.route.port.out

import com.moyeobus.scheduler.domain.route.RouteRequest


data class RouteRequestCluster(
    val acceptedRequests: List<RouteRequest>,
    val exceptedRequests: List<RouteRequest>
)