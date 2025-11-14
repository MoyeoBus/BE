package com.moyeobus.application.route.model

import com.moyeobus.domain.route.Address

data class RequestAddressCount(
    val address: Address,
    val requestCount: Long
)
