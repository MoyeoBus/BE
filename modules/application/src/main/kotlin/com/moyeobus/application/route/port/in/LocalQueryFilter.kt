package com.moyeobus.application.route.port.`in`

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class LocalQueryFilter(
    val dosi: String,
    val sigungu: String,
    val status: String? = null,
    @get:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val from: LocalDateTime? = null,
    @get:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val to: LocalDateTime? = null,
    val cursor: String? = null,
    val limit: Int = 20,
)
