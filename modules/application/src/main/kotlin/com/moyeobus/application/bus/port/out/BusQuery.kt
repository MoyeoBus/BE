package com.moyeobus.application.bus.port.out

import com.fasterxml.jackson.annotation.JsonFormat
import com.moyeobus.domain.bus.BusStatus
import java.time.LocalDateTime

data class BusQuery (
    val status: BusStatus? = null,
    val limit: Int = 9,
    @get:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val cursorCreatedAt: LocalDateTime? = null,
    val cursorId: Long? = null,
)