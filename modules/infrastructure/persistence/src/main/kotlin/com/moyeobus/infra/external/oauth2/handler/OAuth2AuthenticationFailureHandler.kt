package com.moyeobus.infra.external.oauth2.handler

import com.moyeobus.infra.external.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import com.moyeobus.infra.external.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import com.moyeobus.infra.external.oauth2.util.CookieUtil
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class OAuth2AuthenticationFailureHandler(
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository
) : SimpleUrlAuthenticationFailureHandler() {

    @kotlin.Throws(java.io.IOException::class)
    public override fun onAuthenticationFailure(
        request: jakarta.servlet.http.HttpServletRequest, response: jakarta.servlet.http.HttpServletResponse,
        exception: AuthenticationException
    ) {
        var targetUrl: kotlin.String = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
            .map({ obj: jakarta.servlet.http.Cookie? -> obj!!.getValue() })
            .orElse(("/"))
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("error", exception.getLocalizedMessage())
            .build().toUriString()

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)

        getRedirectStrategy().sendRedirect(request, response, targetUrl)
    }
}