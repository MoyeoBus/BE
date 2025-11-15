package com.moyeobus.api.localgov.dto

import com.moyeobus.application.localgov.port.`in`.LocalGovStationWrapper

data class LocalGovStationResult(
    val items: List<LocalGovStationWrapper>
)
