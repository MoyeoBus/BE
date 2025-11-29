package com.moyeobus.infra.external.oauth2.user

import org.springframework.security.core.GrantedAuthority

class GoogleOAuth2UserInfo(
    override val accessToken: String,
    override val attributes: MutableMap<String, Any>
) : OAuth2UserInfo {
    override val provider: OAuth2Provider = OAuth2Provider.GOOGLE
    override val authorities: Collection<GrantedAuthority> = emptyList()

    override val id: String = attributes["sub"] as? String ?: ""
    override val email: String = attributes["email"] as? String ?: ""
    override val name: String = attributes["name"] as? String ?: ""
    override val profileImage: String = attributes["picture"] as? String ?: ""

}