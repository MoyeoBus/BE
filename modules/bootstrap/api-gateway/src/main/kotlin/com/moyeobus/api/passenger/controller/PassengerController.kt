package com.moyeobus.api.passenger.controller

import com.moyeobus.api.docs.PassengerControllerDocs
import com.moyeobus.api.passenger.dto.UserInfoResponse
import com.moyeobus.application.passenger.port.`in`.PassengerQueryUseCase
import com.moyeobus.global.response.ApiResponse
import com.moyeobus.infra.external.auth.security.CustomUserDetails
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/passengers")
class PassengerController(
    private val queryService: PassengerQueryUseCase
) : PassengerControllerDocs {

    @GetMapping("/me")
    override fun getMyInfo(@AuthenticationPrincipal customUserDetails: CustomUserDetails)
    : ResponseEntity<ApiResponse<UserInfoResponse>> {
        val res = queryService.queryByEmail(customUserDetails.email)
        return ResponseEntity.ok(ApiResponse.onSuccess(UserInfoResponse(res.name)))
    }
}