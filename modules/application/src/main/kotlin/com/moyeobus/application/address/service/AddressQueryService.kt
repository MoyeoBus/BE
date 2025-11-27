package com.moyeobus.application.address.service

import com.moyeobus.application.address.dto.StationDto
import com.moyeobus.application.address.port.`in`.AddressQueryUseCase
import com.moyeobus.application.address.port.out.AddressOutPort
import com.moyeobus.application.address.port.out.AreaOutPort
import org.springframework.stereotype.Service

@Service
class AddressQueryService(
    private val repo: AddressOutPort,
    private val areaRepo: AreaOutPort
) : AddressQueryUseCase {
    override fun queryStations(dosi: String,
                               sigungu: String): List<StationDto> {
        val dosiId = areaRepo.findDosiId(dosi).getOrThrow()
        val area = areaRepo.findSigunguByDosi(dosiId, sigungu).getOrThrow()
        val res = repo.findByArea(area)
        return res
    }
}