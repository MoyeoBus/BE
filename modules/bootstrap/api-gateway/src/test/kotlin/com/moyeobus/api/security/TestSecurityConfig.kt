package com.moyeobus.api.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.moyeobus.global.response.ApiResponse
import com.moyeobus.global.response.status.ErrorStatus
import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@TestConfiguration
class TestSecurityConfig(
    private val objectMapper: ObjectMapper
) {

    @Bean
    @Primary
    fun testSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .requestCache { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/api/v1/login",
                    "/api/v1/login/oauth",
                    "/api/v1/signup"
                ).permitAll()
                    .anyRequest().authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint { request, response, _ ->
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.contentType = "application/json;charset=UTF-8"
                    val actualPath = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI) as? String
                        ?: request.requestURI

                    val errorJson = ApiResponse.Companion.onFailure(
                        code = ErrorStatus.UNAUTHORIZED.code,
                        message = ErrorStatus.UNAUTHORIZED.message,
                        data = null,
                        requestUri = actualPath
                    )

                    objectMapper.writeValue(response.outputStream, errorJson)
                }
            }
            .build()
    }
}