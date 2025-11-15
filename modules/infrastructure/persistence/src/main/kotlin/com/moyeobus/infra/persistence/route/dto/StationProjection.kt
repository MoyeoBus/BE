package com.moyeobus.infra.persistence.route.dto

import com.moyeobus.infra.persistence.address.entity.AddressEntity

interface StationProjection {
    val station: AddressEntity
    val count: Int
}