package com.moyeobus.infra.external.oauth2.user

import org.springframework.security.core.GrantedAuthority

class KakaoOAuth2UserInfo(
    override val accessToken: String,
    override val attributes: MutableMap<String, Any>
) : OAuth2UserInfo {

    override val provider: OAuth2Provider = OAuth2Provider.KAKAO
    override val authorities: Collection<GrantedAuthority> = emptyList()

    override val id: String =
        (attributes["id"] as? Long)?.toString() ?: ""

    private val kakaoAccount: Map<String, Any> =
        attributes["kakao_account"] as? Map<String, Any> ?: emptyMap()

    private val kakaoProfile: Map<String, Any> =
        kakaoAccount["profile"] as? Map<String, Any> ?: emptyMap()

    override val email: String =
        kakaoAccount["email"] as? String ?: "glitt5384@naver.com"  // 이메일 동의 아직 안되어 있으면 fallback

    override val name: String =
        kakaoProfile["nickname"] as? String ?: ""

    override val profileImage: String =
        kakaoProfile["profile_image_url"] as? String ?: ""
}