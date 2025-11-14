package com.moyeobus.infra.persistence.route.dto

import com.moyeobus.infra.persistence.address.entity.AddressEntity

interface AddressRankProjection {
    val address: AddressEntity
    val requestCount: Long
}