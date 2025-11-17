package com.moyeobus.api.operator.controller

import com.moyeobus.api.docs.TransportOperatorControllerDocs
import com.moyeobus.api.localgov.dto.OperationResult
import com.moyeobus.api.localgov.dto.RouteTrackResponse
import com.moyeobus.api.localgov.dto.TrackItem
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
import org.springframework.web.bind.annotation.RequestParam
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

    @GetMapping("/{operatorId}/operate")
    override fun getOperateInfo(@PathVariable operatorId: Long) : ResponseEntity<ApiResponse<OperationResult>> {
        val operationCnt = queryService.queryTodayOperate(operatorId)
        val busUsage = queryService.queryBusUsage(operatorId)
        val operationHistory = queryService.queryHistory(operatorId)
        return ResponseEntity.ok(ApiResponse.onSuccess(OperationResult(operationCnt, busUsage, operationHistory)))
    }

    @GetMapping("/{routeId}/track")
    override fun getRouteTracking(
        @PathVariable routeId: Long,
        @RequestParam currentStation: String
    ) : ResponseEntity<ApiResponse<RouteTrackResponse>> {
        val info = queryService.queryRouteTrackInfos(routeId, currentStation)
        val outputs = queryService.queryRouteTrackItems(routeId)
        val points = queryService.queryRouteTrackPoints(routeId)

        val items = outputs.map { TrackItem(it.station, it.time, it.tag) }

        val response = RouteTrackResponse(
            info = info,
            items = items,
            points = points
        )

        return ResponseEntity.ok(ApiResponse.onSuccess(response))
    }
}