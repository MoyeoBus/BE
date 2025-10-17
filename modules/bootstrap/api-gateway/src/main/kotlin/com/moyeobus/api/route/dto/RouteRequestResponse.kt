package com.moyeobus.api.route.dto

import com.moyeobus.domain.route.RouteRequest
import java.time.LocalDateTime


// TODO: 장소 테이블 구성 후 id를 name으로 변경
data class RouteRequestResponse(
    val id: Long? = null,
    val departureId: Long,
    val destinationId: Long,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val status: String,
) {
    companion object {
        fun from (r: RouteRequest) = RouteRequestResponse(
            id = r.id,
            departureId = r.departureId,
            destinationId = r.destinationId,
            startDateTime = r.startDateTime,
            endDateTime = r.endDateTime,
            status = r.status.name
        )
    }
}
