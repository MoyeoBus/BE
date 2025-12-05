package com.moyeobus.scheduler.application.route.port.out

import com.moyeobus.scheduler.application.route.dto.KakaoDirectionRequest
import com.moyeobus.scheduler.application.route.dto.KakaoDirectionResponse


interface KakaoMobilityOutPort {
    fun getDirections(request: KakaoDirectionRequest): KakaoDirectionResponse
}