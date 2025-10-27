package com.moyeobus.application.route.port.`in`

import com.moyeobus.domain.route.Route

interface RouteGenerationUseCase {
    fun generateRoute(): Route
}