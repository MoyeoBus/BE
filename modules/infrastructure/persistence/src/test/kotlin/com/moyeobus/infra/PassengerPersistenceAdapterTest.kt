package com.moyeobus.infra

import com.moyeobus.domain.user.Passenger
import com.moyeobus.global.exception.NotFoundException
import com.moyeobus.infra.persistence.passenger.adapter.PassengerPersistenceAdapter
import com.moyeobus.infra.persistence.passenger.entity.PassengerEntity
import com.moyeobus.infra.persistence.passenger.entity.UserType
import com.moyeobus.infra.persistence.passenger.mapper.PassengerMapper
import com.moyeobus.infra.persistence.passenger.repository.PassengerJpaRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.Optional
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PassengerPersistenceAdapterTest {
    private val repo = mockk<PassengerJpaRepository>()  // Mock!
    private val mapper = mockk<PassengerMapper>()
    private val encoder = mockk<PasswordEncoder>()
    private val adapter = PassengerPersistenceAdapter(mapper, repo, encoder)

    @Test
    fun `사용자가 이미 회원가입된 이메일로 중복 유무를 확인한다 `() {

        // given
        val email = "test1@test.com"
        every { repo.existsByEmail(email) } returns true

        // when
        val res = adapter.existsByEmail(email)

        // then
        assertTrue(res)
        verify(exactly = 1) { repo.existsByEmail(email) }
    }

    @Test
    fun `사용자가 사용 가능한 이메일로 중복 유무를 확인한다`() {
        val email = "test@test.com"
        every { repo.existsByEmail(email) } returns false

        val res = adapter.existsByEmail(email)

        assertFalse(res)
        verify(exactly = 1) { repo.existsByEmail(email) }
    }

    @Test
    fun `실제 존재하는 사용자의 ID로 조회한다`() {
        val id = 1L
        val entity = createPassenger()
        val domain = createPassengerDomain()

        every { repo.findById(id) } returns Optional.of(entity)
        every { mapper.toDomain(entity) } returns domain

        val res = adapter.findById(id)

        assertNotNull(res)
        verify(exactly = 1) { repo.findById(id)}
        verify(exactly = 1) { mapper.toDomain(entity) }
    }

    @Test
    fun `존재하지 않는 사용자 ID로 조회하면 NotFoundException을 던진다`() {
        val id = 999L
        every { repo.findById(id) } returns Optional.empty()

        assertThrows<NotFoundException> { adapter.findById(id) }

        verify(exactly = 1) { repo.findById(id) }
        verify(exactly = 0) { mapper.toDomain(any()) }
    }
    @Test
    fun `회원가입된 이메일로 사용자 조회한다`() {
        val email = "test@test.com"
        val entity = createPassenger()
        val domain = createPassengerDomain()

        every { repo.findByEmail(email) } returns entity
        every { mapper.toDomain(entity)} returns domain

        val res = adapter.findByEmail(email)

        assertNotNull(res)
        verify(exactly = 1) { repo.findByEmail(email) }
        verify(exactly = 1) { mapper.toDomain(entity) }

    }

    @Test
    fun `존재하지 않는 이메일로 조회하면 NotFoundException을 던진다`() {
        val email = "test1@test.com"

        every { repo.findByEmail(email) } returns null

        assertThrows<NotFoundException> { adapter.findByEmail(email) }

        verify(exactly = 1) { repo.findByEmail(email) }
        verify(exactly = 0) { mapper.toDomain(any()) }
    }

    @Test
    fun `새로운 사용자를 저장한다`() {
        val entity = createPassenger(id = null)
        val domain = createPassengerDomain(id = null)
        val savedEntity = createPassenger()
        val savedDomain = createPassengerDomain()

        every { repo.save(entity) } returns savedEntity
        every { mapper.toEntity(domain) } returns entity
        every { repo.findById(1L) } returns Optional.of(savedEntity)
        every { mapper.toDomain(savedEntity) } returns savedDomain

        adapter.save(domain)
        val res = adapter.findById(1L)

        assertNotNull(res)
        assertEquals(1L, res.id)
        verify(exactly = 1) { repo.save(entity) }
        verify(exactly = 1) { mapper.toEntity(domain) }
        verify(exactly = 1) { repo.findById(1L) }
    }


    private fun createPassenger(id: Long? = 1L) = PassengerEntity(
        id = 1L,
        name = "Tester 1",
        email = "test@test.com",
        password = "password",
        autoLoginAgreed = false,
        userType = UserType.LOCAL
    )

    private fun createPassengerDomain(id: Long? = 1L) = Passenger(
        id = 1L,
        name = "Tester 1",
        email = "test@test.com",
        password = "password",
        autoLoginAgreed = false,
        userType = com.moyeobus.domain.user.UserType.LOCAL
    )
}