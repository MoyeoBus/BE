package com.moyeobus.infra.external.oauth2.service

import com.moyeobus.infra.external.oauth2.user.OAuth2UserInfo
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class OAuth2UserPrincipal(private val userInfo: OAuth2UserInfo) : OAuth2User, UserDetails {
    override fun getAttributes(): Map<String, Any> {
        return userInfo.attributes
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return userInfo.authorities
    }

    override fun getName(): String {
        return userInfo.name
    }

    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return userInfo.email
    }

}