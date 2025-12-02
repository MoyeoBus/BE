package com.moyeobus.api.auth

import com.moyeobus.application.auth.port.`in`.SignUpCommand
import com.moyeobus.application.auth.port.`in`.SignUpUseCase
import com.moyeobus.global.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/signup")
class SignUpController(
    private val signUpService: SignUpUseCase
) {

    @PostMapping
    fun signUp(@Validated @RequestBody request: SignUpCommand): ResponseEntity<ApiResponse<Void>>{
        signUpService.processSignUp(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccessCreated())
    }
}