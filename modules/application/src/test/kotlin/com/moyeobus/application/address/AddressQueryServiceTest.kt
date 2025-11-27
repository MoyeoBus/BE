package com.moyeobus.application.address

import com.moyeobus.application.address.dto.StationDto
import com.moyeobus.application.address.port.out.AddressOutPort
import com.moyeobus.application.address.port.out.AreaOutPort
import com.moyeobus.application.address.service.AddressQueryService
import com.moyeobus.domain.route.Area
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class AddressQueryServiceTest {
    private val addressRepo = mockk<AddressOutPort>()
    private val areaRepo = mockk<AreaOutPort>()
    private val service = AddressQueryService(addressRepo, areaRepo)

    @Test
    fun `DB에 있는 도시와 시군구를 조회했을 때 정상적으로 Address 목록을 반환한다`() {
        val dosi = "서울특별시"
        val sigungu = "종로구"

        every { areaRepo.findDosiId(dosi) } returns Result.success(11000L)
        every { areaRepo.findSigunguByDosi(11000, "종로구") } returns Result.success(Area(
            id = 11110,
            sigunguName = "종로구",
            parentSigunguId = 11000
        ))
        every { addressRepo.findByArea(Area(id = 11110,
            sigunguName = "종로구",
            parentSigunguId = 11000)) } returns listOf(
            StationDto(id = 1, name = "경복궁", lat = 37.1234, lon = 128.1234, postCode = "12345"),
            StationDto(id = 2, name = "서울특별시청", lat = 37.12345, lon = 128.12345, postCode = "32312")
            )

        val result = service.queryStations(dosi, sigungu)

        assertEquals(2, result.size)
        assertEquals("경복궁", result[0].name)
        assertEquals("서울특별시청", result[1].name)

        verify { areaRepo.findDosiId("서울특별시") }
        verify { areaRepo.findSigunguByDosi(11000, "종로구") }
        verify { addressRepo.findByArea(any()) }
    }


    @Test
    fun `DB에 없는 도시를 입력하면 예외를 던진다`() {
        val dosi = "인천특별시"
        val sigungu = "중구"

        every { areaRepo.findDosiId(dosi) } returns Result.failure(IllegalArgumentException("유효하지 않은 도시명입니다."))
        assertThrows<IllegalArgumentException> {
            service.queryStations(dosi, sigungu)
        }

        verify { areaRepo.findDosiId("인천특별시") }
    }

    @Test
    fun `DB에 없는 시군구를 입력하면 예외를 던진다`() {
        val dosi = "서울특별시"
        val sigungu = "북구"

        every { areaRepo.findDosiId(dosi) } returns Result.success(11000L)
        every { areaRepo.findSigunguByDosi(11000L, sigungu) } returns Result.failure(IllegalArgumentException("유효하지 않은 시군구명입니다."))
        assertThrows<IllegalArgumentException> {
            service.queryStations(dosi, sigungu)
        }

        verify { areaRepo.findDosiId(dosi) }
        verify { areaRepo.findSigunguByDosi(11000L, sigungu) }
    }

}