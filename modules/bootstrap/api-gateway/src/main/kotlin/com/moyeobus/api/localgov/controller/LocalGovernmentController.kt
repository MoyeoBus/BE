package com.moyeobus.api.localgov.controller

import com.moyeobus.application.localgov.port.out.LocalGovernmentUseCase
import com.moyeobus.global.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/locals")
class LocalGovernmentController(
    private val localGovernmentQueryService: LocalGovernmentUseCase
) {
    @GetMapping("/{localGovId}")
    fun getEntireStatus (
        @PathVariable localGovId: Long
    ) : ResponseEntity<ApiResponse<Any>> {
        return ResponseEntity.ok(ApiResponse.onSuccess(localGovernmentQueryService.queryLocal(localGovId)))
    }
}