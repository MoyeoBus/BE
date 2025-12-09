package com.moyeobus.api.route.controller

import com.moyeobus.api.docs.RouteControllerDocs
import com.moyeobus.api.route.dto.LocalRouteQueryResponse
import com.moyeobus.api.route.dto.PassengerRouteQueryResponse
import com.moyeobus.api.route.dto.QueryResponse
import com.moyeobus.api.route.dto.RouteRequestResponse
import com.moyeobus.api.route.dto.Summary
import com.moyeobus.application.route.model.RouteDetail
import com.moyeobus.application.route.port.`in`.LocalQueryFilter
import com.moyeobus.application.route.port.`in`.QueryFilter
import com.moyeobus.application.route.port.`in`.RouteCommand
import com.moyeobus.application.route.port.`in`.RouteQueryUseCase
import com.moyeobus.application.route.port.`in`.RouteRequestQueryUseCase
import com.moyeobus.application.route.port.`in`.RouteRequestUseCase
import com.moyeobus.application.routeowner.port.`in`.RouteOwnerQueryUseCase
import com.moyeobus.global.response.ApiResponse
import com.moyeobus.infra.external.auth.security.CustomUserDetails
import jakarta.validation.Valid

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
    private val routeQueryService: RouteQueryUseCase,
    private val routeOwnerQueryService: RouteOwnerQueryUseCase,
    private val routeRequestUseCase: RouteRequestUseCase,
    private val routeRequestQueryUseCase: RouteRequestQueryUseCase
) : RouteControllerDocs{

    @GetMapping("/requests")
    override fun query(
        @AuthenticationPrincipal userDetail: CustomUserDetails,
        @RequestParam(required = false) status: String?,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") from: LocalDateTime?,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") to: LocalDateTime?,
        @RequestParam(required = false) cursor: String?,
    ): ResponseEntity<ApiResponse<QueryResponse>> {
        val passengerId = userDetail.id
        val res = routeRequestQueryUseCase.query(passengerId, QueryFilter(status, from, to, cursor))
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

    @GetMapping("/{routeId}/detail")
    override fun queryRouteDetail(@PathVariable routeId: Long) : ResponseEntity<ApiResponse<RouteDetail>> {
        return ResponseEntity.ok(ApiResponse.onSuccess(routeQueryService.queryRouteDetail(routeId)))
    }

    @GetMapping("/local")
    override fun queryLocalRoute(@RequestParam dosi: String,
                         @RequestParam sigungu: String,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") from: LocalDateTime?,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") to: LocalDateTime?,
                         @RequestParam(required = false) cursor: String?) : ResponseEntity<ApiResponse<LocalRouteQueryResponse>> {
        val filter = LocalQueryFilter(dosi, sigungu, null, from, to, cursor)
        val res = routeQueryService.queryLocalRoute(filter)

        return ResponseEntity.ok(ApiResponse.onSuccess(
            LocalRouteQueryResponse(
                items = res.items,
                nextCursor = res.nextCursor,
                hasNext = res.hasNext
            )
        ))
    }

    @GetMapping
    override fun queryByUser(
                    @AuthenticationPrincipal userDetail: CustomUserDetails,
                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") from: LocalDateTime?,
                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") to: LocalDateTime?,
                    @RequestParam(required = false) cursor: String?
    ) : ResponseEntity<ApiResponse<PassengerRouteQueryResponse>> {
        val passengerId = userDetail.id
        val res = routeOwnerQueryService.query(passengerId, QueryFilter( null, from, to, cursor))
        return ResponseEntity.ok(ApiResponse.onSuccess(
            PassengerRouteQueryResponse(
                items = res.items,
                nextCursor = res.nextCursor,
                hasNext = res.hasNext
            )
        ))
    }

    @PostMapping
    override fun create(
        @AuthenticationPrincipal userDetail: CustomUserDetails,
        @Valid @RequestBody command: RouteCommand): ResponseEntity<ApiResponse<Void>> {
        routeRequestUseCase.request(userDetail.id, command)
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