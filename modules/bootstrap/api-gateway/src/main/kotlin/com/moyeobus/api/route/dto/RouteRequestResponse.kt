package com.moyeobus.api.route.dto

import com.moyeobus.domain.route.RouteRequest
import java.time.LocalDateTime


data class RouteRequestResponse(
    val id: Long? = null,
    val departureNm: String,
    val destinationNm: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val status: String,
) {
    companion object {
        fun from (r: RouteRequest) = RouteRequestResponse(
            id = r.id,
            departureNm = r.departure.name,
            destinationNm = r.destination.name,
            startDateTime = r.startDateTime,
            endDateTime = r.endDateTime,
            status = r.status.name
        )
    }
}
