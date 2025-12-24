package com.moyeobus.infra.external.auth

import com.moyeobus.infra.external.oauth2.util.CookieUtil
import com.moyeobus.infra.external.oauth2.util.CookieUtil.expireCookie
import com.moyeobus.infra.external.oauth2.util.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Component

@Component
class CustomLogoutHandler(
    private val tokenBlackListService: TokenBlackListService,
    private val jwtUtil: JwtUtil
) : LogoutHandler {

    override fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication?
    ) {
        val refreshToken = CookieUtil.getRefreshTokenFromRequest(request)

        refreshToken?.let {
            tokenBlackListService.addRefreshTokenBlackList(
                it,
                jwtUtil.getRefreshTokenExpireTime(it)
            )

            expireCookie(response, "refresh", "/", true, true, "Strict")
            expireCookie(response, "access", "/", true, true, "Strict")
        }

        SecurityContextHolder.clearContext()
    }
}