package com.moyeobus.infra.external.oauth2.user

import com.moyeobus.infra.external.oauth2.exception.OAuth2AuthenticationProcessingException

object OAuth2UserInfoFactory {
    fun getOAuth2UserInfo(
        registrationId: String,
        accessToken: String,
        attributes: MutableMap<String, Any>
    ): OAuth2UserInfo {
        if (OAuth2Provider.GOOGLE.provider.equals(registrationId)) {
            return GoogleOAuth2UserInfo(accessToken, attributes)
        }  else if (OAuth2Provider.KAKAO.provider.equals(registrationId)) {
            return KakaoOAuth2UserInfo(accessToken, attributes)
        } else {
            throw OAuth2AuthenticationProcessingException("Login with " + registrationId + " is not supported")
        }
    }
}