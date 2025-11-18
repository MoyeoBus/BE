package com.moyeobus.application.address.port.`in`

import com.moyeobus.application.address.dto.StationDto

interface AddressQueryUseCase {
    fun queryStations(dosi: String,
                      sigungu: String) : List<StationDto>
}