package com.moyeobus.api.passenger

import com.moyeobus.api.security.TestSecurityConfig
import com.moyeobus.api.passenger.controller.PassengerController
import com.moyeobus.application.passenger.port.`in`.PassengerQueryUseCase
import com.moyeobus.domain.user.Passenger
import com.moyeobus.global.exception.NotFoundException
import com.moyeobus.infra.external.auth.security.CustomUserDetails
import com.moyeobus.infra.persistence.passenger.entity.PassengerEntity
import com.moyeobus.infra.persistence.passenger.entity.UserType
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import kotlin.test.Test

@WebMvcTest(PassengerController::class)
@DisplayName("PassengerController 테스트")
@Import(TestSecurityConfig::class)
class PassengerControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var service: PassengerQueryUseCase

    @Nested
    @DisplayName("GET /api/v1/passengers/me")
    inner class GetMyInfo {

        @Test
        @WithMockUser(username = "test1@naver.com")
        fun `인증된 사용자는 자신의 정보를 조회할 수 있다`() {
            val email = "test1@naver.com"
            val name = "테스트사용자1"
            val entity = createPassenger()
            val domain = createPassengerDomain()

            val userDetails = CustomUserDetails(entity)
            val authentication = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities
            )

            every { service.queryByEmail(email) } returns domain

            mockMvc.get("/api/v1/passengers/me") {
                with(SecurityMockMvcRequestPostProcessors.authentication(authentication))
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.result.name") { value(name) }
            }

            verify(exactly = 1) { service.queryByEmail(email) }
        }

        @Test
        fun `인증되지 않은 사용자는 401을 반환한다`() {
            mockMvc.get("/api/v1/passengers/me") {
                with(SecurityMockMvcRequestPostProcessors.authentication(null))
            }
                .andExpect {
                    status { isUnauthorized() }
                }

            verify(exactly = 0) { service.queryByEmail(any()) }
        }

        @Test
        @WithMockUser(username = "illegal@illegal.com")
        fun `존재하지 않는 사용자 조회 시 404를 반환한다`() {
            val email = "illegal@illegal.com"
            val entity = createIllegalPassenger()
            val userDetails = CustomUserDetails(entity)
            val authentication = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities
            )

            every {
                service.queryByEmail(email)
            } throws NotFoundException("Passenger(email=$email)")

            mockMvc.get("/api/v1/passengers/me") {
                with(SecurityMockMvcRequestPostProcessors.authentication(authentication))
            }
                .andExpect {
                    status { isNotFound() }
                }

            verify(exactly = 1) { service.queryByEmail(email) }
        }
    }

    private fun createPassenger() = PassengerEntity(
        id = 1,
        name = "테스트사용자1",
        email = "test1@naver.com",
        password = "password",
        autoLoginAgreed = false,
        userType = UserType.LOCAL
    )

    private fun createIllegalPassenger() = PassengerEntity(
        id = 9999,
        name = "Illegal1",
        email = "illegal@illegal.com",
        password = "password",
        autoLoginAgreed = false,
        userType = UserType.LOCAL
    )

    private fun createPassengerDomain() = Passenger(
        id = 1,
        name = "테스트사용자1",
        email = "test1@naver.com",
        password = "password",
        autoLoginAgreed = false,
        userType = com.moyeobus.domain.user.UserType.LOCAL
    )
}