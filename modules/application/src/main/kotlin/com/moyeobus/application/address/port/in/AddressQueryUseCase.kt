package com.moyeobus.application.address.port.`in`

import com.moyeobus.application.address.dto.SpotDto

interface AddressQueryUseCase {
    fun queryStations(id: Long) : List<SpotDto>
}