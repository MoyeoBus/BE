package com.moyeobus.application.routeowner.port.out

import com.fasterxml.jackson.annotation.JsonFormat
import com.moyeobus.application.routeowner.port.dto.PassengerRouteDto
import java.time.LocalDateTime

data class RouteOwnerPage(
    val items: List<PassengerRouteDto>,
    val hasNext: Boolean,
    @get:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val nextCursorCreatedAt: LocalDateTime?,
    val nextCursorId: Long?,
)