package com.moyeobus.application.route.port.out

import com.moyeobus.application.route.port.out.dto.KakaoDirectionRequest
import com.moyeobus.application.route.port.out.dto.KakaoDirectionResponse

interface KakaoMobilityOutPort {
    fun getDirections(request: KakaoDirectionRequest): KakaoDirectionResponse
}