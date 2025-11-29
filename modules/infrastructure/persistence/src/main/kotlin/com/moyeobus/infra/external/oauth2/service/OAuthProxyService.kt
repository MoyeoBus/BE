package com.moyeobus.infra.external.oauth2.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class OAuthProxyService(
    private val oAuth2Client: WebClient
) {

    fun sendOAuthRequest(provider: String?) {
        oAuth2Client.get()
            .uri({ uriBuilder ->
                uriBuilder
                    .scheme("https")
                    .host("moyeobus.com")
                    .path("/oauth2/authorization/{provider}")
                    .queryParam("access_type", "offline")
                    .queryParam("mode", "login")
                    .queryParam("redirect_uri", "https://moyeobus.com")
                    .build(provider)
            }
            )
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
    }
}
