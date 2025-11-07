package com.moyeobus.api.docs

import com.moyeobus.application.address.dto.StationDto
import com.moyeobus.global.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable

interface AddressControllerDocs {

    @Operation(
        summary = "행정구역 내 정류장(주소) 조회",
        description = "특정 시·군·구(sigunguId)에 포함된 모든 주소(정류장, 주요 지점 등)를 조회합니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "조회 성공 예시",
                                value = """
                                {
                                  "code": "COMMON_200",
                                  "message": "요청이 정상적으로 처리되었습니다.",
                                  "result": [
                                    {
                                      "id": 31,
                                      "name": "제주도청",
                                      "lat": 33.4996,
                                      "lon": 126.5312,
                                      "postCode": "63122"
                                    }
                                  ],
                                  "isSuccess": true
                                }
                                """
                            )
                        ]
                    )
                ]
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 (sigunguId가 숫자가 아님)",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "잘못된 요청 예시",
                                value = """
                                {
                                  "code": "COMMON_400",
                                  "message": "잘못된 요청입니다.",
                                  "result": "sigunguId의 형식을 확인해주세요.",
                                  "isSuccess": false,
                                  "path": "/api/v1/addresses/%EA%B0%80%EB%82%98%EB%8B%A4"
                                }
                                """
                            )
                        ]
                    )
                ]
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "존재하지 않는 행정구역 ID",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "존재하지 않는 ID 예시",
                                value = """
                                {
                                  "code": "COMMON_404",
                                  "message": "Area(id=99999)",
                                  "isSuccess": false,
                                  "path": "/api/v1/addresses/99999"
                                }
                                """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    fun getStations(
        @PathVariable sigunguId: Long
    ): ResponseEntity<ApiResponse<List<StationDto>>>
}