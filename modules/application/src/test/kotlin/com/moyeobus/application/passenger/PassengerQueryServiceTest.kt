package com.moyeobus.application.passenger

import com.moyeobus.application.passenger.port.out.PassengerOutPort
import com.moyeobus.application.passenger.service.PassengerQueryService
import com.moyeobus.domain.user.Passenger
import com.moyeobus.domain.user.UserType
import com.moyeobus.global.exception.NotFoundException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PassengerQueryServiceTest {
    private val repo = mockk<PassengerOutPort>()
    private val service = PassengerQueryService(repo)

    @Test
    fun `회원가입된 이메일로 사용자를 조회한다` () {
        val email = "test1@naver.com"
        every { repo.findByEmail(email) } returns createPassenger()

        val res = service.queryByEmail(email)

        assertNotNull(res)
        assertEquals(email, res.email)
        verify(exactly = 1) { repo.findByEmail(email) }
    }

    @Test
    fun `존재하지 않는 이메일로 사용자를 조회해 NotFoundException을 던진다` () {
        val email = "test2@naver.com"
        every { repo.findByEmail(email) } throws NotFoundException("Passenger(email=$email)")

        assertThrows<NotFoundException> { service.queryByEmail(email) }
        verify(exactly = 1) { repo.findByEmail(email) }
    }

    private fun createPassenger() = Passenger(
        id = 1,
        name = "테스트사용자1",
        email = "test1@naver.com",
        password = "password",
        autoLoginAgreed = false,
        userType = UserType.LOCAL
    )
}