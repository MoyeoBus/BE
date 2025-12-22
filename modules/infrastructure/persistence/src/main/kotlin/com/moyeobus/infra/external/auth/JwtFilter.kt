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
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter(
    private val cookieUtil: CookieUtil,
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService,
    private val tokenBlackListService: TokenBlackListService
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JwtFilter::class.java)
    private val allowOrigins = listOf(
        "/api/v1/login",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/oauth/login",
        "/api/v1/signup",
    )
    private val tokenReissueApi = "/api/v1/tokens"
    private val pathMatcher = AntPathMatcher()



    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val accessToken = CookieUtil.getAccessTokenFromRequest(request)
        val refreshToken = CookieUtil.getRefreshTokenFromRequest(request)

        refreshToken?.let { token ->
            if (tokenBlackListService.isAlreadyBlackListed(token)) {
                expireCookie(response, "access", "/", true, true, "None")
                expireCookie(response, "refresh", "/", true, true, "None")
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "이미 로그아웃한 사용자의 토큰입니다.")
                return
            }

            if (pathMatcher.match(tokenReissueApi, request.requestURI)) {
                val email = jwtUtil.getEmail(refreshToken)
                val reissuedAccess = jwtUtil.createAccess(email)


                response.addCookie(cookieUtil.createAccessCookie(reissuedAccess))
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
            val uri = request.requestURI
            if (allowOrigins.none { pattern -> pathMatcher.match(pattern, uri)}) {
                log.debug("❌ access 토큰 없음, URI=$uri")
            }
        }

        filterChain.doFilter(request, response)
    }
}