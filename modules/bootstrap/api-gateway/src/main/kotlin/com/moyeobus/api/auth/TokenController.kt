package com.moyeobus.api.auth

import com.moyeobus.api.docs.TokenControllerDocs
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/tokens")
class TokenController : TokenControllerDocs{
    @PostMapping
    override fun reissue() { }
}