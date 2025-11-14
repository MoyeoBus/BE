package com.moyeobus.api.operator.controller

import com.moyeobus.api.docs.TransportOperatorControllerDocs
import com.moyeobus.api.operator.dto.RequestAreaRankingResult
import com.moyeobus.api.operator.dto.RequestStationRankingResult
import com.moyeobus.api.operator.dto.RequestSurveyResult
import com.moyeobus.api.operator.dto.RouteDistanceRankingResult
import com.moyeobus.application.transport.port.`in`.TransportOperatorQueryUseCase
import com.moyeobus.application.transport.service.TransportOperatorQueryService
import com.moyeobus.global.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/transports")
class TransportOperatorController(
    private val queryService: TransportOperatorQueryUseCase
) : TransportOperatorControllerDocs{

    @GetMapping("/{operatorId}")
    override fun getRanking(
        @PathVariable operatorId: Long
    ) : ResponseEntity<ApiResponse<RequestAreaRankingResult>> {
        val res = queryService.queryRouteLocalTop5(operatorId)
        return ResponseEntity.ok(ApiResponse.onSuccess(RequestAreaRankingResult(res)))
    }

    @GetMapping("/{operatorId}/stations")
    override fun getStation(
        @PathVariable operatorId: Long
    ) : ResponseEntity<ApiResponse<RequestStationRankingResult>> {
        val res = queryService.queryRouteStationTop5(operatorId)
        return ResponseEntity.ok(ApiResponse.onSuccess(RequestStationRankingResult(res)))
    }

    @GetMapping("/{operatorId}/distances")
    override fun getRouteDistances(
        @PathVariable operatorId: Long
    ) : ResponseEntity<ApiResponse<RouteDistanceRankingResult>> {
        val res = queryService.queryRouteDistanceTop5(operatorId)
        return ResponseEntity.ok(ApiResponse.onSuccess(RouteDistanceRankingResult(res)))
    }
}