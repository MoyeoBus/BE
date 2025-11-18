package com.moyeobus.api.docs

import com.moyeobus.application.address.dto.StationDto
import com.moyeobus.global.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

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
                                      "id": 12,
                                      "name": "둔산동",
                                      "lat": 36.362,
                                      "lon": 127.3565,
                                      "postCode": "35220"
                                    }
                                  ],
                                  "isSuccess": true
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
        @Parameter(example = "대전광역시")
        @RequestParam dosi: String,
        @Parameter(example = "서구")
        @RequestParam sigungu: String
    ): ResponseEntity<ApiResponse<List<StationDto>>>
}