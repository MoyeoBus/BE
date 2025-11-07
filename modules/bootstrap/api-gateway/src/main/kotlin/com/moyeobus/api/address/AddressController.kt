package com.moyeobus.api.address

import com.moyeobus.api.docs.AddressControllerDocs
import com.moyeobus.application.address.dto.StationDto
import com.moyeobus.application.address.port.`in`.AddressQueryUseCase
import com.moyeobus.global.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/addresses")
class AddressController(
    private val addressQueryService: AddressQueryUseCase
) : AddressControllerDocs {

    @GetMapping("/{sigunguId}")
    override fun getStations(@PathVariable sigunguId: Long) : ResponseEntity<ApiResponse<List<StationDto>>> {
        return ResponseEntity.ok(ApiResponse.onSuccess(addressQueryService.queryStations(sigunguId)))
    }
}