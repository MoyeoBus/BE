package com.moyeobus.api.localgov.controller

import com.moyeobus.api.docs.LocalGovernmentControllerDocs
import com.moyeobus.application.localgov.port.`in`.LocalGovDateResult
import com.moyeobus.application.localgov.port.`in`.LocalGovRouteResult
import com.moyeobus.application.localgov.port.`in`.LocalGovStatusResult
import com.moyeobus.application.localgov.port.`in`.LocalGovTimeResult
import com.moyeobus.application.localgov.port.`in`.LocalGovernmentUseCase
import com.moyeobus.global.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate


@RestController
@RequestMapping("/api/v1/locals")
class LocalGovernmentController(
    private val localGovernmentQueryService: LocalGovernmentUseCase
) : LocalGovernmentControllerDocs {

    @GetMapping("/{localGovId}")
    override fun getEntireStatus (
        @PathVariable localGovId: Long
    ) : ResponseEntity<ApiResponse<LocalGovStatusResult>> {
        return ResponseEntity.ok(ApiResponse.onSuccess(localGovernmentQueryService.queryLocal(localGovId)))
    }

    @GetMapping("/{localGovId}/date")
    override fun getStatusByDate (
        @PathVariable localGovId: Long
    ) : ResponseEntity<ApiResponse<LocalGovDateResult>> {
        return ResponseEntity.ok(ApiResponse.onSuccess(localGovernmentQueryService.queryDate(localGovId)))
    }

    @GetMapping("/{localGovId}/time")
    override fun getStatusByTime (
        @PathVariable localGovId: Long,
        @RequestParam stdDate: LocalDate
    ) : ResponseEntity<ApiResponse<LocalGovTimeResult>> {
        return ResponseEntity.ok(ApiResponse.onSuccess(localGovernmentQueryService.queryHour(localGovId, stdDate)))
    }

    @GetMapping("/{localGovId}/route")
    override fun getStatusByRoute (
        @PathVariable localGovId: Long
    ) : ResponseEntity<ApiResponse<LocalGovRouteResult>> {
        return ResponseEntity.ok(ApiResponse.onSuccess(localGovernmentQueryService.queryRoute(localGovId)))
    }
}