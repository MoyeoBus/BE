package com.moyeobus.application.route.port.out.dto

import com.moyeobus.domain.route.Address

data class KakaoDirectionRequest(
    val origin: Point,
    val destination: Point,
    val waypoints: List<Waypoint>? = emptyList(),
    val priority: String = "RECOMMEND"
) {
    companion object {
        /**
         * ✅ Address → KakaoDirectionRequest (단일 요청 버전)
         * - origin = 출발 Address
         * - destination = 도착 Address
         */
        fun fromSingle(start: Address, end: Address): KakaoDirectionRequest {
            return KakaoDirectionRequest(
                origin = Point(start.lat.toString(), start.lon.toString()),
                destination = Point(end.lat.toString(), end.lon.toString()),
                waypoints = emptyList(),
                priority = "RECOMMEND"
            )
        }
    }
}