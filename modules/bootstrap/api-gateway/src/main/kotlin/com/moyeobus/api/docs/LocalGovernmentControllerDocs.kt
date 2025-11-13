package com.moyeobus.api.docs

import com.moyeobus.application.localgov.port.`in`.LocalGovDateResult
import com.moyeobus.application.localgov.port.`in`.LocalGovRouteResult
import com.moyeobus.application.localgov.port.`in`.LocalGovStatusResult
import com.moyeobus.application.localgov.port.`in`.LocalGovTimeResult
import com.moyeobus.global.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

interface LocalGovernmentControllerDocs {

    @Operation(
        summary = "지자체 전체 통계 조회",
        description = "특정 지자체(localGovId)의 사용률을 파이차트로 조회합니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "조회 성공 예시",
                            value = """
                            {
                              "code": "COMMON_200",
                              "message": "요청이 정상적으로 처리되었습니다.",
                              "result": {
                                "localName": "서울특별시",
                                "items": [
                                  {
                                    "areaId": 11110,
                                    "sigunguName": "종로구",
                                    "count": 1,
                                    "ratio": "50.00"
                                  },
                                  {
                                    "areaId": 11140,
                                    "sigunguName": "중구",
                                    "count": 1,
                                    "ratio": "50.00"
                                  }
                                ]
                              },
                              "isSuccess": true
                            }
                            """
                        )
                    ]
                )]
            )
        ]
    )
    fun getEntireStatus(
        @Parameter(example = "1", description = "조회할 지자체 ID")
        @PathVariable localGovId: Long
    ): ResponseEntity<ApiResponse<LocalGovStatusResult>>


    @Operation(
        summary = "지자체 일자별 통계 조회",
        description = "지자체의 날짜별 노선 요청 통계를 조회합니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "조회 성공 예시",
                            value = """
                            {
                              "code": "COMMON_200",
                              "message": "요청이 정상적으로 처리되었습니다.",
                              "result": {
                                "govName": "서울특별시",
                                "data": [
                                  {
                                    "date": "2025-11-06",
                                    "useCount": 4
                                  }
                                ]
                              },
                              "isSuccess": true
                            }
                            """
                        )
                    ]
                )]
            )
        ]
    )
    fun getStatusByDate(
        @Parameter(example = "1", description = "조회할 지자체 ID")
        @PathVariable localGovId: Long
    ): ResponseEntity<ApiResponse<LocalGovDateResult>>


    @Operation(
        summary = "지자체 시간대별 통계 조회",
        description = "지정된 날짜(stdDate)의 시간대별 노선 요청 통계를 조회합니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "조회 성공 예시",
                            value = """
                            {
                              "code": "COMMON_200",
                              "message": "요청이 정상적으로 처리되었습니다.",
                              "result": {
                                "govName": "서울특별시",
                                "data": [
                                  {
                                    "hour": 8,
                                    "useCount": 2
                                  }
                                ]
                              },
                              "isSuccess": true
                            }
                            """
                        )
                    ]
                )]
            )
        ]
    )
    fun getStatusByTime(
        @Parameter(example = "1", description = "조회할 지자체 ID")
        @PathVariable localGovId: Long,

        @Parameter(example = "2025-11-10", description = "조회 기준 날짜 (yyyy-MM-dd)")
        @RequestParam
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        stdDate: LocalDate
    ): ResponseEntity<ApiResponse<LocalGovTimeResult>>


    @Operation(
        summary = "지자체 노선별 통계 조회",
        description = "지자체의 노선(route)별 요청 통계를 조회합니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = [Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "조회 성공 예시",
                            value = """
                            {
                              "code": "COMMON_200",
                              "message": "요청이 정상적으로 처리되었습니다.",
                              "result": {
                                "govName": "서울특별시",
                                "items": [
                                  {
                                    "routeId": 166,
                                    "stationCount": 7,
                                    "peopleCount": 1,
                                    "distance": 1655
                                  },
                                  {
                                    "routeId": 167,
                                    "stationCount": 7,
                                    "peopleCount": 1,
                                    "distance": 1655
                                  },
                                  {
                                    "routeId": 168,
                                    "stationCount": 31,
                                    "peopleCount": 1,
                                    "distance": 39499
                                  },
                                  {
                                    "routeId": 169,
                                    "stationCount": 7,
                                    "peopleCount": 2,
                                    "distance": 1655
                                  }
                                ]
                              },
                              "isSuccess": true
                            }
                            """
                        )
                    ]
                )]
            )
        ]
    )
    fun getStatusByRoute(
        @Parameter(example = "1", description = "조회할 지자체 ID")
        @PathVariable localGovId: Long
    ): ResponseEntity<ApiResponse<LocalGovRouteResult>>
}