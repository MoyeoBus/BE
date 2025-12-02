package com.moyeobus.infra.external.auth


import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeobus.application.auth.port.`in`.LoginCommand
import com.moyeobus.infra.exception.NotFoundException
import com.moyeobus.infra.external.auth.security.CustomUserDetails
import com.moyeobus.infra.external.oauth2.util.CookieUtil
import com.moyeobus.infra.external.oauth2.util.JwtUtil
import com.moyeobus.infra.persistence.user.repository.PassengerJpaRepository
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component

import org.springframework.util.StreamUtils;


import java.nio.charset.StandardCharsets;

class LoginFilter(
    authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val cookieUtil: CookieUtil,
    private val passengerRepository: PassengerJpaRepository
) : UsernamePasswordAuthenticationFilter() {

    init {
        super.setAuthenticationManager(authenticationManager)
        setFilterProcessesUrl("/api/v1/login")
    }

    private val logger = org.slf4j.LoggerFactory.getLogger(LoginFilter::class.java)

    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Authentication {

        return try {
            val messageBody = StreamUtils.copyToString(request.inputStream, StandardCharsets.UTF_8)
            val loginRequest = ObjectMapper().readValue(messageBody, LoginCommand::class.java)

            val passenger = passengerRepository.findByEmail(loginRequest.email)?:
            throw NotFoundException("Passenger(email=$loginRequest.email)")

            if (passenger.userType.toString() != "LOCAL") {
                throw RuntimeException("${passenger.userType} 계정으로 가입된 회원입니다.")
            }

            val authToken = UsernamePasswordAuthenticationToken(
                loginRequest.email,
                loginRequest.password,
                null
            )

            authenticationManager.authenticate(authToken)

        } catch (e: Exception) {
            throw RuntimeException("Failed to process authentication", e)
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authentication: Authentication
    ) {
        val userDetails = authentication.principal as CustomUserDetails
        val email = userDetails.email

        try {
            val access = jwtUtil.createAccess(email)
            val refresh = jwtUtil.createRefresh(email)


            response.addCookie(cookieUtil.createCookie("access", access))
            response.addCookie(cookieUtil.createCookie("refresh", refresh))
            response.status = HttpStatus.OK.value()

        } catch (e: Exception) {
            logger.error("토큰 생성 중 오류 발생: ${e.message}", e)
            response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
            response.contentType = "application/json"
            response.writer.write("""{"error": "로그인 처리 중 오류가 발생했습니다."}""")
        }
    }
}