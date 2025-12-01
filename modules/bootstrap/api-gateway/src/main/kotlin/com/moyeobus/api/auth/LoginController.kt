package com.moyeobus.api.auth

import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/login")
class LoginController {

    @GetMapping("/oauth")
    fun redirectToProvider(
        @RequestParam provider: String,
        response: HttpServletResponse
    ) {
        val redirectUrl = String.format(
            "http://localhost:8080/oauth2/authorization/%s",
            provider
        )
        println("redirectUrl=" +redirectUrl)
        response.sendRedirect(redirectUrl)
    }
}