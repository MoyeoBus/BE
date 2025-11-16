package com.moyeobus.api.docs

import com.moyeobus.api.localgov.dto.LocalGovStationResult
import com.moyeobus.api.localgov.dto.RouteTrackResponse
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
import java.time.YearMonth

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
                                    "date": "2025-11-01",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-02",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-03",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-04",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-05",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-06",
                                    "useCount": 4
                                  },
                                  {
                                    "date": "2025-11-07",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-08",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-09",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-10",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-11",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-12",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-13",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-14",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-15",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-16",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-17",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-18",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-19",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-20",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-21",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-22",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-23",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-24",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-25",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-26",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-27",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-28",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-29",
                                    "useCount": 0
                                  },
                                  {
                                    "date": "2025-11-30",
                                    "useCount": 0
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
        @PathVariable localGovId: Long,
        @RequestParam stdDate: YearMonth,
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
                                    "hour": 5,
                                    "useCount": 0
                                  },
                                  {
                                    "hour": 6,
                                    "useCount": 0
                                  },
                                  {
                                    "hour": 7,
                                    "useCount": 0
                                  },
                                  {
                                    "hour": 8,
                                    "useCount": 2
                                  },
                                  {
                                    "hour": 9,
                                    "useCount": 0
                                  },
                                  {
                                    "hour": 10,
                                    "useCount": 0
                                  },
                                  {
                                    "hour": 11,
                                    "useCount": 0
                                  },
                                  {
                                    "hour": 12,
                                    "useCount": 0
                                  },
                                  {
                                    "hour": 13,
                                    "useCount": 0
                                  },
                                  {
                                    "hour": 14,
                                    "useCount": 0
                                  },
                                  {
                                    "hour": 15,
                                    "useCount": 0
                                  },
                                  {
                                    "hour": 16,
                                    "useCount": 0
                                  },
                                  {
                                    "hour": 17,
                                    "useCount": 0
                                  },
                                  {
                                    "hour": 18,
                                    "useCount": 0
                                  },
                                  {
                                    "hour": 19,
                                    "useCount": 0
                                  },
                                  {
                                    "hour": 20,
                                    "useCount": 0
                                  },
                                  {
                                    "hour": 21,
                                    "useCount": 0
                                  },
                                  {
                                    "hour": 22,
                                    "useCount": 0
                                  },
                                  {
                                    "hour": 23,
                                    "useCount": 0
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

    @Operation(
        summary = "출발지 기준 요청 통계 조회",
        description = "특정 노선(routeId)에 대해 출발지 기준으로 요청량을 집계하여 정류장별 통계를 반환합니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "출발지 통계 조회 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "출발지 통계 성공 예시",
                                value = """
                                {
                                  "code": "COMMON_200",
                                  "message": "요청이 정상적으로 처리되었습니다.",
                                  "result": {
                                    "items": [
                                      {
                                        "stationName": "경복궁",
                                        "count": 1
                                      }
                                    ]
                                  },
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
    fun getStatusByDeparture(
        @Parameter(description = "조회 대상 노선 ID", example = "42")
        @PathVariable routeId: Long
    ): ResponseEntity<ApiResponse<LocalGovStationResult>>


    @Operation(
        summary = "도착지 기준 요청 통계 조회",
        description = "특정 노선(routeId)에 대해 도착지 기준으로 요청량을 집계하여 정류장별 통계를 반환합니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "도착지 통계 조회 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "도착지 통계 성공 예시",
                                value = """
                                {
                                  "code": "COMMON_200",
                                  "message": "요청이 정상적으로 처리되었습니다.",
                                  "result": {
                                    "items": [
                                      {
                                        "stationName": "경복궁",
                                        "count": 1
                                      }
                                    ]
                                  },
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
    fun getStatusByDestination(
        @Parameter(description = "조회 대상 노선 ID", example = "42")
        @PathVariable routeId: Long
    ): ResponseEntity<ApiResponse<LocalGovStationResult>>

    @Operation(
        summary = "노선 추적 정보 조회",
        description = "특정 노선(routeId) 기준으로 현재 정류장을 포함한 노선 추적 정보를 조회합니다."
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
                                name = "노선 추적 조회 성공 예시",
                                summary = "정상 조회된 노선 추적 정보",
                                value =
                                    """
                                {
                                  "code": "COMMON_200",
                                  "message": "요청이 정상적으로 처리되었습니다.",
                                  "result": {
                                    "info": {
                                      "routeId": 219,
                                      "nextStation": "광화문",
                                      "gapTime": 252,
                                      "remainDistance": 1056
                                    },
                                    "items": [
                                      {
                                        "station": "서울특별시청",
                                        "time": "08:00",
                                        "tag": "출발"
                                      },
                                      {
                                        "station": "광화문",
                                        "time": "08:00",
                                        "tag": "종점"
                                      }
                                    ],
                                    "points": [
                                      {
                                        "lat": 126.97775402959569,
                                        "lon": 37.56651331084051
                                      },
                                      {
                                        "lat": 126.97790509874943,
                                        "lon": 37.567055243687506
                                      },
                                      {
                                        "lat": 126.97728271413804,
                                        "lon": 37.56704084315428
                                      },
                                      {
                                        "lat": 126.97705826414102,
                                        "lon": 37.5751752958502
                                      },
                                      {
                                        "lat": 126.97479764602366,
                                        "lon": 37.57574136046887
                                      },
                                      {
                                        "lat": 126.97414607226652,
                                        "lon": 37.5770241866185
                                      },
                                      {
                                        "lat": 126.97489741654962,
                                        "lon": 37.57837326265234
                                      }
                                    ]
                                  },
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
    fun getRouteTracking(
        @Parameter(description = "조회할 노선의 ID", example = "173")
        @PathVariable routeId: Long,

        @Parameter(description = "현재 차량이 위치한 정류장 이름", example = "서울역")
        @RequestParam currentStation: String
    ): ResponseEntity<ApiResponse<RouteTrackResponse>>
}