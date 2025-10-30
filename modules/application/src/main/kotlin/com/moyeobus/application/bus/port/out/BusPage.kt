package com.moyeobus.application.bus.port.out

import com.fasterxml.jackson.annotation.JsonFormat
import com.moyeobus.domain.bus.Bus
import java.time.LocalDateTime

data class BusPage(
    val items: List<Bus>,
    val hasNext: Boolean,
    @get:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val nextCursorCreatedAt: LocalDateTime?,
    val nextCursorId: Long?,
)
