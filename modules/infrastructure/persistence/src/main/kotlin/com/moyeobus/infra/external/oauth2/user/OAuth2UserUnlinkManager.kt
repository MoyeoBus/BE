package com.moyeobus.infra.external.oauth2.user

import com.moyeobus.infra.external.oauth2.exception.OAuth2AuthenticationProcessingException
import org.springframework.stereotype.Component

@Component
class OAuth2UserUnlinkManager(
    private val googleOAuth2UserUnlink: GoogleOAuth2UserUnlink? = null,
    private val kakaoOAuth2UserUnlink: KakaoOAuth2UserUnlink? = null
) {

    fun unlink(provider: OAuth2Provider, accessToken: String) {
        if (OAuth2Provider.GOOGLE == provider) {
            googleOAuth2UserUnlink!!.unlink(accessToken)
        } else if (OAuth2Provider.KAKAO == provider) {
            kakaoOAuth2UserUnlink!!.unlink(accessToken)
        } else {
            throw OAuth2AuthenticationProcessingException(
                "Unlink with " + provider.provider + " is not supported"
            )
        }
    }
}