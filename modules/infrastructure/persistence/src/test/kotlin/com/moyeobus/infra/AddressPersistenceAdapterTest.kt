package com.moyeobus.infra

import com.moyeobus.domain.route.Area
import com.moyeobus.infra.persistence.address.entity.AreaEntity
import com.moyeobus.infra.persistence.address.repotiory.AddressJpaRepository
import com.moyeobus.infra.persistence.address.repotiory.AreaJpaRepository
import com.moyeobus.infra.persistence.config.JpaConfig
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import kotlin.test.Test
import kotlin.test.assertEquals

@ActiveProfiles("dev")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = [JpaConfig::class])
class AddressPersistenceAdapterTest @Autowired constructor(
    val addressRepo: AddressJpaRepository,
    val areaRepo: AreaJpaRepository
) {
    @Test
    fun `DB에 존재하는 지역을 입력받아 지역에 속한 정류장을 반환한다`() {
        val area = areaRepo.findById(11000).get()
        val res = addressRepo.findByArea(area)

        assertThat(res).isNotNull
    }

    @Test
    fun `DB에 존재하지 않는 지역을 입력받아 지역에 속한 빈 배열을 반환한다`() {
        val area = AreaEntity(id = 99999, "서울광역시", null)
        val res = addressRepo.findByArea(area)

        assertThat(res).isEmpty()
    }

    @Test
    fun `DB에 존재하는 areaId들을 입력받아 해당 지역들에 속한 정류장을 반환한다`() {
        val areaIds = listOf(11000L, 11110L, 28000L)
        val res = addressRepo.findAllByAreaIdIn(areaIds)

        assertEquals(3, res.size)
    }

    @Test
    fun `DB에 존재하지 않는 areaId를 포함하여 입력받아 해당 지역들에 속한 정류장을 반환한다`() {
        val areaIds = listOf(99999L, 11110L, 28000L)
        val res = addressRepo.findAllByAreaIdIn(areaIds)

        assertEquals(2, res.size)
    }
}