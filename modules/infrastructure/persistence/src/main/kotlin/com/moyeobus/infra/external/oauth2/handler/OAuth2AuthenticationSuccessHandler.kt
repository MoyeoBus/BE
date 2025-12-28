package com.moyeobus.infra.external.oauth2.handler

import com.moyeobus.infra.external.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import com.moyeobus.infra.external.oauth2.service.OAuth2UserPrincipal
import com.moyeobus.infra.external.oauth2.user.OAuth2UserUnlinkManager
import com.moyeobus.infra.external.oauth2.util.CookieUtil
import com.moyeobus.infra.external.oauth2.util.JwtUtil
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class OAuth2AuthenticationSuccessHandler(
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,
    private val oAuth2UserUnlinkManager: OAuth2UserUnlinkManager,
    private val jwtUtil: JwtUtil,
    private val authorizedClientService: OAuth2AuthorizedClientService

) : SimpleUrlAuthenticationSuccessHandler() {

    private val FRONTEND_URL = "https://app.moyeobus.com"

    override fun onAuthenticationSuccess(
        request: jakarta.servlet.http.HttpServletRequest, response: jakarta.servlet.http.HttpServletResponse,
        authentication: Authentication
    ) {
        val targetUrl = determineTargetUrl(request, response, authentication)

        val principal: OAuth2UserPrincipal? = getOAuth2UserPrincipal(authentication)

        if (principal != null) {
            val accessToken = jwtUtil.createAccess(principal.username)
            val refreshToken = jwtUtil.createRefresh(principal.username)
            val oauthToken: OAuth2AuthenticationToken = authentication as OAuth2AuthenticationToken
            val client: OAuth2AuthorizedClient = authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName()
            )


            val googleAccessToken: kotlin.String? = client.getAccessToken().getTokenValue()

            response.addHeader("access", accessToken)
            response.addHeader("refresh", refreshToken)
            CookieUtil.addCookie(response, "access_token", accessToken, 3600)
            CookieUtil.addCookie(response, "google_oauth_token", googleAccessToken, 3600)
            CookieUtil.addCookie(response, "refresh_token", refreshToken, 86400)

        }

        if (response.isCommitted()) {
            return
        }

        val redirectUrl = UriComponentsBuilder
            .fromUriString(FRONTEND_URL)
            .build()
            .toUriString()

        clearAuthenticationAttributes(request, response)

        getRedirectStrategy().sendRedirect(request, response, redirectUrl)
    }

    override fun determineTargetUrl(
        request: jakarta.servlet.http.HttpServletRequest, response: jakarta.servlet.http.HttpServletResponse,
        authentication: Authentication
    ): kotlin.String {
        val targetUrl = "https://app.moyeobus.com"


        val principal: OAuth2UserPrincipal? = getOAuth2UserPrincipal(authentication)
        if (principal == null) {
            return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login failed")
                .build().toUriString()
        }


        val access = jwtUtil.createAccess(principal.username)
        val refresh = jwtUtil.createRefresh(principal.username)

        CookieUtil.addCookie(response, "access_token", access, 3600)
        CookieUtil.addCookie(response, "refresh_token", refresh, 86400)

        return UriComponentsBuilder.fromUriString(targetUrl)
            .build().toUriString()
    }

    private fun getOAuth2UserPrincipal(authentication: Authentication): OAuth2UserPrincipal? {
        val principal = authentication.getPrincipal()

        if (principal is OAuth2UserPrincipal) {
            return principal as OAuth2UserPrincipal?
        }
        return null
    }

    protected fun clearAuthenticationAttributes(
        request: jakarta.servlet.http.HttpServletRequest,
        response: jakarta.servlet.http.HttpServletResponse
    ) {
        super.clearAuthenticationAttributes(request)
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }
}