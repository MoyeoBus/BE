package com.moyeobus.application.route.port.out

import com.fasterxml.jackson.annotation.JsonFormat
import com.moyeobus.application.route.port.`in`.QueryFilter
import com.moyeobus.domain.route.RequestStatus
import java.time.LocalDateTime

data class RouteRequestSummaryFilter(
    val status: RequestStatus? = null,
    @get:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val from: LocalDateTime? = null,
    @get:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val to: LocalDateTime? = null,
) {
    companion object {
        fun from(q: QueryFilter) = RouteRequestSummaryFilter(
            status = q.status?.let { RequestStatus.valueOf(it) },
            from = q.from,
            to = q.to
        )
    }
}
