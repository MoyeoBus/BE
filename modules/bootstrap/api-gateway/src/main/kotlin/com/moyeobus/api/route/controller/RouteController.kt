package com.moyeobus.api.route.controller

import com.moyeobus.application.route.port.`in`.RouteCommand
import com.moyeobus.application.route.port.`in`.RouteRequestResult
import com.moyeobus.application.route.port.`in`.RouteUseCase
import com.moyeobus.global.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/routes")
class RouteController(
    private val routeUseCase: RouteUseCase
) {

    @GetMapping
    fun query(): ResponseEntity<ApiResponse<RouteRequestResult>> {
        val res = routeUseCase.query()
        return ResponseEntity
            .ok(ApiResponse.onSuccess(res))
    }

    @PostMapping
    fun create(@RequestBody command: RouteCommand): ResponseEntity<ApiResponse<Void>> {
        routeUseCase.request(command)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.Companion.onSuccessVoid())
    }

    @PatchMapping("/{requestId}")
    fun cancel(
        @PathVariable
        requestId: Long): ResponseEntity<ApiResponse<Void>> {
        routeUseCase.cancel(requestId)
        return ResponseEntity
            .ok()
            .body(ApiResponse.Companion.onSuccessVoid());
    }

}