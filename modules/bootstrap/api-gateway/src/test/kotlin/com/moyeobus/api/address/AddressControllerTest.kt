package com.moyeobus.api.address

import com.moyeobus.api.MoyeoBusApplication
import com.moyeobus.application.address.dto.StationDto
import com.moyeobus.global.response.ApiResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("dev")
class AddressControllerTest @Autowired constructor(
    private val restTemplate: TestRestTemplate
){

    @LocalServerPort
    private lateinit var port: Integer

    @Test
    fun `주소 조회 API — 정상 응답 확인`() {
        val url = "http://localhost:$port/api/v1/addresses?dosi=서울특별시&sigungu=종로구"

        val response: ResponseEntity<ApiResponse<List<StationDto>>> =
            restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<ApiResponse<List<StationDto>>>() {}
            )

        val expected = ApiResponse(
            isSuccess = true,
            code = "COMMON_200",
            message = "요청이 정상적으로 처리되었습니다.",
            result = listOf(StationDto(
                id=2, name="경복궁", lat=37.5796, lon=126.977, postCode="03045"
            )),
            path = null
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body)
            .usingRecursiveComparison()
            .isEqualTo(expected)
    }

}