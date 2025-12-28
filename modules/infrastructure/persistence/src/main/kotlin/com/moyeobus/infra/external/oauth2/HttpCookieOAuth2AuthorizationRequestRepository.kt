package com.moyeobus.infra.external.oauth2

import com.moyeobus.infra.external.oauth2.util.CookieUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@Component
class HttpCookieOAuth2AuthorizationRequestRepository :
    AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? =
        CookieUtil.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            .map { cookie -> CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest::class.java) }
            .orElse(null)

    override fun saveAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest?,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        if (authorizationRequest == null) {
            deleteAllCookies(request, response)
            return
        }

        CookieUtil.addCookie(
            response,
            OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
            CookieUtil.serialize(authorizationRequest),
            COOKIE_EXPIRE_SECONDS
        )

        request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME)?.takeIf { StringUtils.hasText(it) }?.let {
            CookieUtil.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, it, COOKIE_EXPIRE_SECONDS)
            println("redirect_uri 쿠키 저장됨: $it")
        } ?: println("redirect_uri 쿠키 없음")

        request.getParameter(MODE_PARAM_COOKIE_NAME)?.takeIf { StringUtils.hasText(it) }?.let {
            CookieUtil.addCookie(response, MODE_PARAM_COOKIE_NAME, it, COOKIE_EXPIRE_SECONDS)
            println("mode 쿠키 저장됨: $it")
        } ?: println("mode 쿠키 없음")
    }

    override fun removeAuthorizationRequest(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): OAuth2AuthorizationRequest? = loadAuthorizationRequest(request)

    fun deleteAllCookies(request: HttpServletRequest, response: HttpServletResponse) {
        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        CookieUtil.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME)
        CookieUtil.deleteCookie(request, response, MODE_PARAM_COOKIE_NAME)
    }

    fun removeAuthorizationRequestCookies(request: HttpServletRequest, response: HttpServletResponse) {
        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        CookieUtil.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME)
        CookieUtil.deleteCookie(request, response, MODE_PARAM_COOKIE_NAME)
    }

    companion object {
        const val OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"
        const val REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri"
        const val MODE_PARAM_COOKIE_NAME = "mode"
        private const val COOKIE_EXPIRE_SECONDS = 180
    }
}