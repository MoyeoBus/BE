package com.moyeobus.application.route.port.`in`

import com.moyeobus.application.route.port.out.dto.KakaoDirectionResponse
import com.moyeobus.domain.route.Route

interface RouteGenerationUseCase {
    fun generateRoute(): List<Any>
}