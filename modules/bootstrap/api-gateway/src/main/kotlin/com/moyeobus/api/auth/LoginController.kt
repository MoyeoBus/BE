package com.moyeobus.api.auth

import com.moyeobus.api.docs.LoginControllerDocs
import com.moyeobus.application.auth.port.`in`.LoginCommand
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/login")
class LoginController() : LoginControllerDocs{

    @PostMapping
    override fun localLogin(@Valid @RequestBody request: LoginCommand) {}

    @PostMapping("/oauth")
    override fun redirectToProvider(
        @RequestParam provider: String,
        response: HttpServletResponse
    ) {
        val redirectUrl = String.format(
            "https://app.moyeobus.com/oauth2/authorization/%s",
            provider
        )
        response.sendRedirect(redirectUrl)
    }
}