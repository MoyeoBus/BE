package com.moyeobus.application.passenger

import com.moyeobus.application.passenger.port.out.PassengerOutPort
import com.moyeobus.application.passenger.service.PassengerQueryService
import com.moyeobus.domain.user.Passenger
import com.moyeobus.domain.user.UserType
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class PassengerQueryServiceTest {
    private val repo = mockk<PassengerOutPort>()
    private val service = PassengerQueryService()

    @Test
    fun `이메일로 사용자를 조회한다` () {
        every { repo.findByEmail("test1@naver.com") } returns createPassenger()
        assertEquals(1, createPassenger().id)
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