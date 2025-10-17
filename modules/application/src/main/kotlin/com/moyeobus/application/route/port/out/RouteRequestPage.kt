package com.moyeobus.application.route.port.out

import com.fasterxml.jackson.annotation.JsonFormat
import com.moyeobus.domain.route.RouteRequest
import java.time.LocalDateTime

data class RouteRequestPage(
    val items: List<RouteRequest>,
    val hasNext: Boolean,
    @get:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val nextCursorCreatedAt: LocalDateTime?,
    val nextCursorId: Long?,
)
