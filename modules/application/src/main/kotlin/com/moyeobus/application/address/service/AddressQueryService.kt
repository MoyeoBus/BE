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
    override fun queryStations(id: Long): List<StationDto> {
        val area = areaRepo.findById(id)
        val res = repo.findByArea(area)
        return res
    }
}