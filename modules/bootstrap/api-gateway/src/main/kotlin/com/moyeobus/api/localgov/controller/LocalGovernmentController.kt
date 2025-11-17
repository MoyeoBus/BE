package com.moyeobus.api.localgov.controller

import com.moyeobus.api.docs.LocalGovernmentControllerDocs
import com.moyeobus.api.localgov.dto.LocalGovStationResult
import com.moyeobus.api.localgov.dto.SurveyPieResult
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
import java.time.YearMonth


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
        @PathVariable localGovId: Long,
        @RequestParam stdDate: YearMonth
    ) : ResponseEntity<ApiResponse<LocalGovDateResult>> {
        return ResponseEntity.ok(ApiResponse.onSuccess(localGovernmentQueryService.queryDate(localGovId, stdDate)))
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
    ) : ResponseEntity<ApiResponse<LocalGovRouteResult>>  {
        return ResponseEntity.ok(ApiResponse.onSuccess(localGovernmentQueryService.queryRoute(localGovId)))
    }

    @GetMapping("/{routeId}/departures")
    override fun getStatusByDeparture (
        @PathVariable routeId: Long
    ) : ResponseEntity<ApiResponse<LocalGovStationResult>> {
        val response = LocalGovStationResult(localGovernmentQueryService.queryDeparture(routeId))
        return ResponseEntity.ok(ApiResponse.onSuccess(response))
    }

    @GetMapping("/{routeId}/destinations")
    override fun getStatusByDestination (
        @PathVariable routeId: Long
    ) : ResponseEntity<ApiResponse<LocalGovStationResult>> {
        val response = LocalGovStationResult(localGovernmentQueryService.queryDestination(routeId))
        return ResponseEntity.ok(ApiResponse.onSuccess(response))
    }

    @GetMapping("/{localGovId}/survey")
    override fun getSurveyByLocalGov (
        @PathVariable localGovId: Long
    ) : ResponseEntity<ApiResponse<SurveyPieResult>> {
        val res = localGovernmentQueryService.querySurvey(localGovId)
        val response = SurveyPieResult(res)
        return ResponseEntity.ok(ApiResponse.onSuccess(response))
    }

}