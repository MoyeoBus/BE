package com.moyeobus.application.localgov.port.`in`

data class LocalGovStatusResult(
    val localName: String,
    val items: List<LocalGovStatusWrapper>
)
