package com.moyeobus.application.localgov.port.`in`

data class LocalGovStatusWrapper(
    val areaId: Long,
    val sigunguName: String,
    val count: Int,
    val ratio: String
)