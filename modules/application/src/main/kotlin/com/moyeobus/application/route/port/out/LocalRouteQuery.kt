package com.moyeobus.application.route.port.out

import com.fasterxml.jackson.annotation.JsonFormat
import com.moyeobus.application.route.port.`in`.LocalQueryFilter
import com.moyeobus.domain.route.RequestStatus
import com.moyeobus.global.util.CursorWrapper
import java.time.LocalDateTime

data class LocalRouteQuery(
    val dosi: String,
    val sigungu: String,
    val status: RequestStatus? = null,
    @get:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val from: LocalDateTime? = null,
    @get:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val to: LocalDateTime? = null,
    val limit: Int = 5,
    @get:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val cursorCreatedAt: LocalDateTime? = null,
    val cursorId: Long? = null,
) {
    companion object {
        fun from(q: LocalQueryFilter, p: CursorWrapper) = LocalRouteQuery(
            dosi = q.dosi,
            sigungu = q.sigungu,
            status = q.status?.let { RequestStatus.valueOf(it) },
            from = q.from,
            to = q.to,
            limit = q.limit,
            cursorCreatedAt = p.cursorCreatedAt?.let {
                LocalDateTime.ofInstant(it, java.time.ZoneOffset.UTC)
            },
            cursorId = p.cursorId
        )
    }
}
