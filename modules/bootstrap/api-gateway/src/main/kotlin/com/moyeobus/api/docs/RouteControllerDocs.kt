package com.moyeobus.api.docs

import com.moyeobus.api.route.dto.LocalRouteQueryResponse
import com.moyeobus.api.route.dto.PassengerRouteQueryResponse
import com.moyeobus.api.route.dto.QueryResponse
import com.moyeobus.application.route.model.RouteDetail
import com.moyeobus.application.route.port.`in`.RouteCommand
import com.moyeobus.global.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime

@Tag(
    name = "노선 API",
    description = """
    🗺️ 노선 도메인에 연관된 CRUD API 그룹
    """
)
interface RouteControllerDocs {

    @Operation(
        summary = "노선 요청 생성",
        description = "새로운 노선 요청을 생성합니다. <br/> 요청 생성 성공 시 201 Created 상태 코드가 반환됩니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "201",
                description = "노선 요청 생성 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "요청 성공",
                                value = """
                                {
                                  "code": "COMMON_201",
                                  "message": "데이터가 정상적으로 생성되었습니다.",
                                  "isSuccess": true,
                                }
                                """
                            )
                        ]
                    )
                ]
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 본문",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "요청 실패 예시",
                                summary = "필수 필드 누락",
                                value = """
                                {
                                    "code": "COMMON_400",
                                    "message": "잘못된 요청입니다.",
                                    "result": "Instantiation of [simple type, class com.moyeobus.application.route.port.in.RouteCommand] value failed for JSON property startDateTime due to missing (therefore NULL) value for creator parameter startDateTime which is a non-nullable type\n at [Source: REDACTED (`StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION` disabled); line: 5, column: 1] (through reference chain: com.moyeobus.application.route.port.in.RouteCommand[\"startDateTime\"])",
                                    "isSuccess": false,
                                    "path": "/api/v1/routes"
                                }
                                """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    fun create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "노선 요청에 필요한 데이터",
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "기본 요청 예시",
                            value = """
                            {
                              "departureId": 1,
                              "destinationId": 2,
                              "startDateTime": "2025-10-17T10:00:00",
                              "endDateTime": "2025-10-17T12:00:00"
                            }
                            """
                        )
                    ]
                )
            ]
        )
        @RequestBody
        command: RouteCommand
    ): ResponseEntity<ApiResponse<Void>>


    @Operation(
        summary = "노선 요청 조회",
        description = "필터 조건에 맞는 노선 요청 내역을 페이지네이션 형태로 반환합니다."
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
                                summary = "노선 요청 목록, 요약 정보, 커서 포함",
                                value = """
                                {
                                  "code": "COMMON_200",
                                  "message": "요청이 정상적으로 처리되었습니다.",
                                  "result": {
                                    "items": [
                                      {
                                        "id": 9,
                                        "departureNm": "천안시청",
                                        "destinationNm": "보령시청",
                                        "startDateTime": "2025-11-06T09:00:00",
                                        "endDateTime": "2025-11-06T09:40:00",
                                        "status": "APPROVED"
                                      },
                                      {
                                        "id": 8,
                                        "departureNm": "청주시청",
                                        "destinationNm": "충주시청",
                                        "startDateTime": "2025-11-06T08:30:00",
                                        "endDateTime": "2025-11-06T09:10:00",
                                        "status": "APPROVED"
                                      },
                                      {
                                        "id": 7,
                                        "departureNm": "여수시청",
                                        "destinationNm": "목포시청",
                                        "startDateTime": "2025-11-06T08:00:00",
                                        "endDateTime": "2025-11-06T08:30:00",
                                        "status": "APPROVED"
                                      },
                                      {
                                        "id": 6,
                                        "departureNm": "목포시청",
                                        "destinationNm": "군산시청",
                                        "startDateTime": "2025-11-06T09:00:00",
                                        "endDateTime": "2025-11-06T09:40:00",
                                        "status": "APPROVED"
                                      },
                                      {
                                        "id": 5,
                                        "departureNm": "군산시청",
                                        "destinationNm": "전주시청",
                                        "startDateTime": "2025-11-06T08:30:00",
                                        "endDateTime": "2025-11-06T09:20:00",
                                        "status": "APPROVED"
                                      },
                                      {
                                        "id": 4,
                                        "departureNm": "전주시청",
                                        "destinationNm": "여수시청",
                                        "startDateTime": "2025-11-06T08:00:00",
                                        "endDateTime": "2025-11-06T08:50:00",
                                        "status": "APPROVED"
                                      },
                                      {
                                        "id": 3,
                                        "departureNm": "안양역",
                                        "destinationNm": "수원시청",
                                        "startDateTime": "2025-11-06T09:00:00",
                                        "endDateTime": "2025-11-06T09:40:00",
                                        "status": "APPROVED"
                                      },
                                      {
                                        "id": 2,
                                        "departureNm": "수원시청",
                                        "destinationNm": "서울특별시청",
                                        "startDateTime": "2025-11-06T08:30:00",
                                        "endDateTime": "2025-11-06T09:10:00",
                                        "status": "APPROVED"
                                      },
                                      {
                                        "id": 1,
                                        "departureNm": "서울특별시청",
                                        "destinationNm": "경복궁",
                                        "startDateTime": "2025-11-06T08:00:00",
                                        "endDateTime": "2025-11-06T08:40:00",
                                        "status": "APPROVED"
                                      }
                                    ],
                                    "summary": {
                                      "totalCount": 9,
                                      "approvedCount": 9,
                                      "cancelledCount": 0,
                                      "pendingCount": 0
                                    },
                                    "nextCursor": null,
                                    "hasNext": false
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
    fun query(
        @PathVariable passengerId: Long,

        @Parameter(example = "APPROVED", description = "노선 요청 상태 (APPROVED, CANCELLED, PENDING)")
        @RequestParam(required = false) status: String?,

        @Parameter(example = "2025-10-01 00:00:00", description = "조회 시작일 (yyyy-MM-dd HH:mm:ss)")
        @RequestParam(required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        from: LocalDateTime?,

        @Parameter(example = "2025-10-31 23:59:59", description = "조회 종료일 (yyyy-MM-dd HH:mm:ss)")
        @RequestParam(required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        to: LocalDateTime?,

        @Parameter(example = "MTc1OTY1ODA1MDcwMzo1", description = "페이지네이션 커서 값(초기 시행값은 null)")
        @RequestParam(required = false)
        cursor: String?,
    ): ResponseEntity<ApiResponse<QueryResponse>>

    @Operation(
        summary = "노선 상세 조회",
        description = "특정 `routeId`에 대한 노선 상세 정보를 반환합니다."
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
                                name = "상세 조회 성공",
                                value = """
                            {
                                "code": "COMMON_200",
                                "message": "요청이 정상적으로 처리되었습니다.",
                                "result": {
                                    "routeInfo": {
                                        "busNumber": 1,
                                        "departureName": "서울역",
                                        "destinationName": "강남역",
                                        "operateDate": "2025-11-10",
                                        "departTime": "08:00",
                                        "arrivalTime": "08:45"
                                    },
                                    "items": [
                                        {
                                            "order": 1,
                                            "station": "서울역",
                                            "time": "08:00",
                                            "isDestinationYn": false
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
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "해당 routeId 없음"
            )
        ]
    )
    fun queryRouteDetail(
        @Parameter(example = "1", description = "조회할 노선 ID")
        @PathVariable routeId: Long
    ): ResponseEntity<ApiResponse<RouteDetail>>

    @Operation(
        summary = "지역 기반 노선 조회",
        description = "도/시(`dosi`)와 시/군/구(`sigungu`), 날짜 범위(`from`,`to`), 커서를 기준으로 노선 목록을 조회합니다."
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
                                name = "지역 노선 조회 성공",
                                value = """
                            {
                                "code": "COMMON_200",
                                "message": "요청이 정상적으로 처리되었습니다.",
                                "result": {
                                    "items": [
                                        {
                                            "routeId": 173,
                                            "operateDate": "2025-11-10",
                                            "departure": "서울역",
                                            "destination": "강남역",
                                            "distance": 41539,
                                            "duration": 2785
                                        }
                                    ],
                                    "nextCursor": null,
                                    "hasNext": false
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
    fun queryLocalRoute(
        @Parameter(example = "서울특별시", description = "도/시 명")
        @RequestParam dosi: String,

        @Parameter(example = "종로구", description = "시/군/구 명")
        @RequestParam sigungu: String,

        @Parameter(example = "2025-10-01 00:00:00", description = "조회 시작일 (yyyy-MM-dd HH:mm:ss)")
        @RequestParam(required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        from: LocalDateTime?,

        @Parameter(example = "2025-11-01 23:59:59", description = "조회 종료일 (yyyy-MM-dd HH:mm:ss)")
        @RequestParam(required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        to: LocalDateTime?,

        @Parameter(example = "MTc1OTY1ODA1MDcwMzo1", description = "커서 기반 페이지네이션 cursor")
        @RequestParam(required = false)
        cursor: String?,
    ): ResponseEntity<ApiResponse<LocalRouteQueryResponse>>


    @Operation(
        summary = "사용자별 노선 조회",
        description = "특정 승객(`passengerId`)이 참여하거나 생성한 노선 목록을 조회합니다. <br/> 상태(`status`), 날짜 필터(`from`, `to`), 페이지네이션(`cursor`)를 사용할 수 있습니다."
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
                              "result": {
                                "items": [
                                  {
                                    "routeId": 216,
                                    "departure": "군산시청",
                                    "destination": "목적지",
                                    "operatedDate": "2025-11-06",
                                    "assignedTime": [
                                      "08:30"
                                    ],
                                    "status": "CREATED"
                                  },
                                  {
                                    "routeId": 215,
                                    "departure": "전주시청",
                                    "destination": "목적지",
                                    "operatedDate": "2025-11-06",
                                    "assignedTime": [
                                      "08:00"
                                    ],
                                    "status": "CREATED"
                                  }
                                ],
                                "nextCursor": null,
                                "hasNext": false
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
    fun queryByUser(
        @Parameter(example = "1", description = "조회할 승객 ID")
        @PathVariable passengerId: Long,

        @Parameter(example = "2025-11-01 00:00:00", description = "조회 시작일 (yyyy-MM-dd HH:mm:ss)")
        @RequestParam(required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        from: LocalDateTime?,

        @Parameter(example = "2025-11-30 23:59:59", description = "조회 종료일 (yyyy-MM-dd HH:mm:ss)")
        @RequestParam(required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        to: LocalDateTime?,

        @Parameter(example = "MTc1OTY2NzExMDAwMDoxMA==", description = "페이지네이션 커서 값")
        @RequestParam(required = false)
        cursor: String?
    ): ResponseEntity<ApiResponse<PassengerRouteQueryResponse>>


    @Operation(
        summary = "노선 요청 취소",
        description = "특정 노선 요청(`requestId`)을 취소합니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "취소 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "취소 성공 예시",
                                value = """
                                {
                                    "code": "COMMON_200",
                                    "message": "요청이 정상적으로 처리되었습니다.",
                                    "isSuccess": true
                                }
                                """
                            )
                        ]
                    )
                ]
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "405",
                description = "PathVariable 누락",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "요청 실패 예시",
                                summary = "PathVariable을 기입하지 않은 채로 요청 전송",
                                value = """
                                {
                                  "timestamp": "2025-10-31T12:00:00.000+09:00",
                                  "status": 404,
                                  "error": "Not Found",
                                  "path": "/api/v1/routes"
                                }
                                """
                            )
                        ]
                    )
                ]
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "해당 요청 ID를 가진 데이터를 찾을 수 없음",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "요청 실패 예시",
                                summary = "존재하지 않는 requestId",
                                value = """
                                {
                                    "code": "COMMON_500",
                                    "message": "서버 에러, 관리자에게 문의 바랍니다.",
                                    "result": "RouteRequest not found: 1",
                                    "isSuccess": false,
                                    "path": "/api/v1/routes/1"
                                }
                                """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    fun cancel(
        @Parameter(example = "1", description = "취소할 요청의 ID")
        @PathVariable requestId: Long
    ): ResponseEntity<ApiResponse<Void>>
}