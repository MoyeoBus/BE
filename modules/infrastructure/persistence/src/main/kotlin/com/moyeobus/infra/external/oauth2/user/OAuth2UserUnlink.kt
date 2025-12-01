package com.moyeobus.infra.external.oauth2.user

interface OAuth2UserUnlink {
    fun unlink(accessToken: String)
}