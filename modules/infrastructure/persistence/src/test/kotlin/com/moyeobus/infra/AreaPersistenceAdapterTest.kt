package com.moyeobus.infra

import com.moyeobus.infra.persistence.address.repotiory.AreaJpaRepository
import com.moyeobus.infra.persistence.config.JpaConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration


@ActiveProfiles("dev")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = [JpaConfig::class])
class AreaPersistenceAdapterTest @Autowired constructor(
    val areaRepo: AreaJpaRepository,
){

    @Test
    fun `DB에 존재하는 도시명을 넣으면 해당 도시 id를 반환한다`() {
        val res = areaRepo.findDosiId("서울특별시")
        assertEquals(11000, res)
    }

    @Test
    fun `DB에 존재하지 않는 도시명을 넣으면 null을 반환한다`() {
        val res = areaRepo.findDosiId("서울광역시")
        assertThat(res).isNull()
    }

    @Test
    fun `DB에 존재하는 도시와 시군구 문자열로 시군구 객체를 반환한다`() {
        val res = areaRepo.findSigunguByDosi(11000, "종로구")
        assertEquals(11110, res!!.id)
    }

    @Test
    fun `DB에 존재하지 않는 도시와 시군구 문자열을 넣으면 null을 반환한다`() {
        val res = areaRepo.findSigunguByDosi(11000, "북구")
        assertThat(res).isNull()
    }

    @Test
    fun `DB에 존재하는 상위 행정구역의 지역 코드를 넣으면 하위 행정구역을 모두 반환한다`() {
        val res = areaRepo.findAllChildren(11000)
        val parents = res.map { it.parentSigunguId }.toSet()
        assertEquals(1, parents.size)
    }

    @Test
    fun `DB에 존재하지 않는 상위 행정구역의 지역 코드를 넣으면 빈 배열을 반환한다`() {
        val res = areaRepo.findAllChildren(99999)
        assertThat(res).isEmpty()
    }

}