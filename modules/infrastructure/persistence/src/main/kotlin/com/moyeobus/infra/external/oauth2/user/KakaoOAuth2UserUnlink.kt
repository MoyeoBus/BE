package com.moyeobus.infra.external.oauth2.user

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class KakaoOAuth2UserUnlink(
    private val restTemplate: RestTemplate
) : OAuth2UserUnlink {

    override fun unlink(accessToken: String) {
        val headers = HttpHeaders().apply {
            setBearerAuth(accessToken)
        }

        val entity = HttpEntity("", headers)

        restTemplate.exchange(
            URL,
            HttpMethod.POST,
            entity,
            String::class.java
        )
    }

    companion object {
        private const val URL = "https://kapi.kakao.com/v1/user/unlink"
    }
}