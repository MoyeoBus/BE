package com.moyeobus.api.auth

import com.moyeobus.application.auth.port.`in`.SignUpUseCase
import jakarta.servlet.http.HttpServletResponse

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/login")
class LoginController() {

    @GetMapping("/oauth")
    fun redirectToProvider(
        @RequestParam provider: String,
        response: HttpServletResponse
    ) {
        val redirectUrl = String.format(
            "https://www.moyeobus.com/oauth2/authorization/%s",
            provider
        )
        response.sendRedirect(redirectUrl)
    }
}