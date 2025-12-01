package com.moyeobus.infra.external.oauth2.config

import com.moyeobus.infra.external.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import com.moyeobus.infra.external.oauth2.handler.OAuth2AuthenticationFailureHandler
import com.moyeobus.infra.external.oauth2.handler.OAuth2AuthenticationSuccessHandler
import com.moyeobus.infra.external.oauth2.service.CustomOAuth2UserService
import com.moyeobus.infra.external.oauth2.util.CookieUtils
import com.moyeobus.infra.external.oauth2.util.JwtUtil
import com.moyeobus.infra.persistence.user.repository.PassengerJpaRepository
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

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
    private val cookieUtil: CookieUtils
    //private val userDetailsService: UserDetailsService
) {

    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager =
        configuration.authenticationManager

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

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

        http.csrf { it.disable() }

        http.formLogin { it.disable() }
        http.httpBasic { it.disable() }

        http.exceptionHandling {
            it.authenticationEntryPoint { _, response, _ ->
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.contentType = "application/json"
                response.writer.write("{\"error\": \"Unauthorized request\"}")
            }
        }

        http.authorizeHttpRequests {
            it.requestMatchers(
                "/oauth2/**", "/register/*", "/login",
                "/swagger-ui/**", "/v3/api-docs/**",
                 "/socket/**", "/api/**",
                "/oauth/login"
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

//        http.addFilterBefore(JwtFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter::class.java)
//        http.addFilterAt(
//            LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, cookieUtil, userRepository),
//            UsernamePasswordAuthenticationFilter::class.java
//        )
//        http.addFilterBefore(CustomLogoutFilter(jwtUtil), LogoutFilter::class.java)

        http.sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        return http.build()
    }
}