package com.moyeobus.infra.external.oauth2.user

import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Component
class GoogleOAuth2UserUnlink(
    private val restTemplate: RestTemplate
): OAuth2UserUnlink {

    override fun unlink(accessToken: String) {
        val params: MultiValueMap<String?, String?> =
            LinkedMultiValueMap<String?, String?>()
        params.add("token", accessToken)
        restTemplate.postForObject<kotlin.String?>(
            GoogleOAuth2UserUnlink.Companion.URL,
            params,
            kotlin.String::class.java
        )
    }

    companion object {
        private const val URL = "https://oauth2.googleapis.com/revoke"
    }
}