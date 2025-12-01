package com.moyeobus.application.auth.port.`in`

import jakarta.validation.constraints.NotBlank

data class LoginCommand(
    @param:NotBlank
    val email: String,
    @param:NotBlank
    val password: String
)