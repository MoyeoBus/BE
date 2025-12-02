package com.moyeobus.infra.external.auth

import com.moyeobus.infra.external.oauth2.util.CookieUtil
import com.moyeobus.infra.external.oauth2.util.CookieUtil.expireCookie
import com.moyeobus.infra.external.oauth2.util.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService,
    private val tokenBlackListService: TokenBlackListService
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JwtFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val accessToken = CookieUtil.getAccessTokenFromRequest(request)
        val refreshToken = CookieUtil.getRefreshTokenFromRequest(request)


        refreshToken?.let {
            if (tokenBlackListService.isAlreadyBlackListed(it)) {
                expireCookie(response, "access", "/", true, true, "None")
                expireCookie(response, "refresh", "/", true, true, "None")
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "이미 로그아웃한 사용자의 토큰입니다.")
                return
            }
        }

        accessToken?.let { token ->
            if (!jwtUtil.isExpired(token)) {
                val email = jwtUtil.getEmail(token)
                val userDetails = userDetailsService.loadUserByUsername(email)

                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                ).apply {
                    details = WebAuthenticationDetailsSource().buildDetails(request)
                }

                SecurityContextHolder.getContext().authentication = authentication
            } else {
                log.debug("❌ JWT 토큰 만료됨")
            }
        } ?: run {
            log.debug("❌ access 토큰 없음, URI=${request.requestURI}")
        }

        filterChain.doFilter(request, response)
    }
}