package com.moyeobus.api.auth

import com.moyeobus.api.docs.LogoutControllerDocs
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/logout")
class LogoutController : LogoutControllerDocs {
    @PostMapping
    override fun logout() {}
}