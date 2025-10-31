package com.moyeobus.api.route.controller

import com.moyeobus.api.docs.RouteControllerDocs
import com.moyeobus.api.route.dto.QueryResponse
import com.moyeobus.api.route.dto.RouteRequestResponse
import com.moyeobus.api.route.dto.Summary
import com.moyeobus.application.route.port.`in`.QueryFilter
import com.moyeobus.application.route.port.`in`.RouteCommand
import com.moyeobus.application.route.port.`in`.RouteGenerationUseCase
import com.moyeobus.application.route.port.`in`.RouteRequestQueryUseCase
import com.moyeobus.application.route.port.`in`.RouteRequestUseCase
import com.moyeobus.global.response.ApiResponse
import jakarta.validation.Valid

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/routes")
class RouteController(
    private val routeRequestUseCase: RouteRequestUseCase,
    private val routeRequestQueryUseCase: RouteRequestQueryUseCase,
    private val routeGenerationUseCase: RouteGenerationUseCase,
) : RouteControllerDocs{

    @GetMapping("/test")
    fun test() : ResponseEntity<Any> {
        return ResponseEntity.ok(routeGenerationUseCase.generateRoute())
    }

    @GetMapping
    override fun query(
        @RequestParam(required = false) status: String?,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") from: LocalDateTime?,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") to: LocalDateTime?,
        @RequestParam(required = false) cursor: String?,
    ): ResponseEntity<ApiResponse<QueryResponse>> {
        val res = routeRequestQueryUseCase.query(QueryFilter(status, from, to, cursor))
        return ResponseEntity.ok(ApiResponse.onSuccess(
            QueryResponse(
                items = res.items.map { RouteRequestResponse.from(it) },
                summary = Summary(res.summary.totalCount, res.summary.approvedCount,
                    res.summary.cancelledCount, res.summary.pendingCount),
                nextCursor = res.nextCursor,
                hasNext = res.hasNext
            )
        ))
    }

    @PostMapping
    override fun create(@Valid @RequestBody command: RouteCommand): ResponseEntity<ApiResponse<Void>> {
        routeRequestUseCase.request(command)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.Companion.onSuccessCreated())
    }

    @PatchMapping("/{requestId}")
    override fun cancel(@PathVariable requestId: Long): ResponseEntity<ApiResponse<Void>> {
        routeRequestUseCase.cancel(requestId)
        return ResponseEntity
            .ok()
            .body(ApiResponse.Companion.onSuccessVoid());
    }

}