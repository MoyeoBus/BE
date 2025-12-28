package com.moyeobus.infra.external.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.moyeobus.global.response.ApiResponse
import com.moyeobus.global.response.status.ErrorStatus
import com.moyeobus.infra.external.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import com.moyeobus.infra.external.oauth2.handler.OAuth2AuthenticationFailureHandler
import com.moyeobus.infra.external.oauth2.handler.OAuth2AuthenticationSuccessHandler
import com.moyeobus.infra.external.oauth2.service.CustomOAuth2UserService
import com.moyeobus.infra.external.oauth2.util.CookieUtil
import com.moyeobus.infra.external.oauth2.util.JwtUtil
import com.moyeobus.infra.persistence.user.repository.PassengerJpaRepository
import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
    private val oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler,
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,

    private val authenticationConfiguration: AuthenticationConfiguration,
    private val jwtUtil: JwtUtil,
    private val userRepository: PassengerJpaRepository,
    private val cookieUtil: CookieUtil,
    private val userDetailsService: UserDetailsService,
    private val tokenBlackListService: TokenBlackListService
) {

    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager =
        configuration.authenticationManager

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun loginFilter(): LoginFilter {
        return LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, cookieUtil, userRepository)
    }

    @Bean
    fun logoutHandler(): LogoutHandler {
        return CustomLogoutHandler(tokenBlackListService, jwtUtil)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowedOrigins = listOf(
            "http://localhost:5173",
            "https://moyeobus.com",
            "https://api.moyeobus.com",
            "https://app.moyeobus.com",
            "https://moyeo-bus-fe-web.vercel.app",
            "https://moyeo-bus-fe.vercel.app"
        )
        config.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        config.allowedHeaders = listOf("*")
        config.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Bean
    fun httpExchangeRepository(): HttpExchangeRepository = InMemoryHttpExchangeRepository()

    @Bean
    @Order(1)
    fun actuatorSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/actuator/**")
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }
            .csrf { it.disable() }

        return http.build()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors { it.configurationSource(corsConfigurationSource())}
        http.csrf { it.disable() }

        http.formLogin { it.disable() }
        http.httpBasic { it.disable() }

        http.exceptionHandling {
            it.authenticationEntryPoint { request, response, _ ->
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.contentType = "application/json"
                val actualPath = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI) as? String
                    ?: request.requestURI

                val errorJson = ApiResponse.onFailure(
                    code = ErrorStatus.UNAUTHORIZED.code,
                    message = ErrorStatus.UNAUTHORIZED.message,
                    data = null,
                    requestUri = actualPath
                )

                ObjectMapper().writeValue(response.outputStream, errorJson)
            }
        }
        http.authorizeHttpRequests {
            it.requestMatchers(
                "/oauth2/**",
                "/swagger-ui/**", "/v3/api-docs/**",
                 "/socket/**", "/api/v1/login", "/api/v1/login/oauth",
                "/api/v1/signup", "/api/v1/oauth2/**"
            ).permitAll()
                .anyRequest().authenticated()
        }

        http.oauth2Login {
            it.authorizationEndpoint { auth ->
                auth.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
            }
                .userInfoEndpoint { userInfo ->
                    userInfo.userService(customOAuth2UserService)
                }
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
        }

        http.addFilterBefore(JwtFilter(cookieUtil, jwtUtil, userDetailsService, tokenBlackListService), UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterAt(
            loginFilter(),
            UsernamePasswordAuthenticationFilter::class.java
        )
        http.logout { logout ->
            logout
                .logoutUrl("/api/v1/logout")
                .addLogoutHandler(logoutHandler())
                .logoutSuccessHandler { request, response, authentication ->
                    response.status = 204
                }
        }

        http.sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        return http.build()
    }
}