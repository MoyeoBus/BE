package com.moyeobus.infra.external.oauth2.user

import org.springframework.security.core.GrantedAuthority

interface OAuth2UserInfo {
    val provider: OAuth2Provider

    val accessToken: String

    val attributes: MutableMap<String, Any>

    val authorities: Collection<GrantedAuthority>

    val id: String

    val email: String

    val name: String

    val profileImage: String
}