package com.moyeobus.api.docs

import com.moyeobus.api.localgov.dto.OperationResult
import com.moyeobus.api.localgov.dto.RouteTrackResponse
import com.moyeobus.api.operator.dto.RequestAreaRankingResult
import com.moyeobus.api.operator.dto.RequestStationRankingResult
import com.moyeobus.api.operator.dto.RouteDistanceRankingResult
import com.moyeobus.global.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

interface TransportOperatorControllerDocs {

    @Operation(
        summary = "지역 기반 요청 랭킹 TOP 5 조회",
        description = "운수사(operatorId) 기준으로 출발지/도착지 모든 요청을 집계하여 지역별 랭킹 TOP 5를 반환합니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "지역 랭킹 조회 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "지역 랭킹 성공 예시",
                                value = """
                                {
                                  "code": "COMMON_200",
                                  "message": "요청이 정상적으로 처리되었습니다.",
                                  "result": {
                                    "items": [
                                      {
                                        "areaName": "서울특별시",
                                        "ranking": 1,
                                        "requestCount": 1
                                      },
                                      {
                                        "areaName": "종로구",
                                        "ranking": 2,
                                        "requestCount": 1
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
    fun getRanking(
        @Parameter(example = "1", description = "운수사 ID")
        @PathVariable operatorId: Long
    ): ResponseEntity<ApiResponse<RequestAreaRankingResult>>


    @Operation(
        summary = "정류장 기반 요청 랭킹 TOP 5 조회",
        description = "운수사(operatorId) 기준으로 출발지/도착지의 정류장 요청을 집계하여 TOP 5를 반환합니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "정류장 랭킹 조회 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "정류장 랭킹 성공 예시",
                                value = """
                                {
                                  "code": "COMMON_200",
                                  "message": "요청이 정상적으로 처리되었습니다.",
                                  "result": {
                                    "items": [
                                      {
                                        "stationName": "서울특별시청",
                                        "ranking": 1,
                                        "requestCount": 1
                                      },
                                      {
                                        "stationName": "경복궁",
                                        "ranking": 2,
                                        "requestCount": 1
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
    fun getStation(
        @Parameter(example = "1", description = "운수사 ID")
        @PathVariable operatorId: Long
    ): ResponseEntity<ApiResponse<RequestStationRankingResult>>


    @Operation(
        summary = "노선 거리 TOP 5 조회",
        description = "운수사(operatorId)가 보유한 노선 중 이동 거리(routeDistance)가 가장 긴 Top 5 목록을 조회합니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "노선 거리 랭킹 조회 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "노선 거리 랭킹 성공 예시",
                                value = """
                                {
                                  "code": "COMMON_200",
                                  "message": "요청이 정상적으로 처리되었습니다.",
                                  "result": {
                                    "items": [
                                      {
                                        "routeNo": 173,
                                        "ranking": 1,
                                        "distance": 41539
                                      },
                                      {
                                        "routeNo": 175,
                                        "ranking": 2,
                                        "distance": 41539
                                      },
                                      {
                                        "routeNo": 178,
                                        "ranking": 3,
                                        "distance": 41539
                                      },
                                      {
                                        "routeNo": 180,
                                        "ranking": 4,
                                        "distance": 41539
                                      },
                                      {
                                        "routeNo": 182,
                                        "ranking": 5,
                                        "distance": 41539
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
    fun getRouteDistances(
        @Parameter(example = "1", description = "운수사 ID")
        @PathVariable operatorId: Long
    ): ResponseEntity<ApiResponse<RouteDistanceRankingResult>>


    @Operation(
        summary = "운수사 운행 현황 조회",
        description = "특정 운수사(operatorId)의 오늘 운행 횟수, 버스 운행 현황, 최근 운행 이력을 조회합니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "운수사 운행 현황 조회 성공",
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
    @GetMapping("/{operatorId}/operate")
    fun getOperateInfo(
        @Parameter(description = "운수사 ID", required = true)
        @PathVariable operatorId: Long
    ): ResponseEntity<ApiResponse<OperationResult>>

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