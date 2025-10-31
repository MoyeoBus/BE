package com.moyeobus.api.docs

import com.moyeobus.api.route.dto.QueryResponse
import com.moyeobus.application.route.port.`in`.RouteCommand
import com.moyeobus.global.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime

interface RouteControllerDocs {

    @Operation(
        summary = "노선 요청 생성",
        description = "새로운 노선 요청을 생성합니다. 요청 생성 성공 시 201 Created 상태 코드가 반환됩니다."
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
                                                "id": 6,
                                                "departureId": 1,
                                                "destinationId": 2,
                                                "startDateTime": "2025-10-31T12:00:00",
                                                "endDateTime": "2025-10-31T14:00:00",
                                                "status": "PENDING"
                                            },
                                            {
                                                "id": 5,
                                                "departureId": 1,
                                                "destinationId": 2,
                                                "startDateTime": "2025-10-31T12:00:00",
                                                "endDateTime": "2025-10-31T14:00:00",
                                                "status": "PENDING"
                                            },
                                            {
                                                "id": 4,
                                                "departureId": 1,
                                                "destinationId": 2,
                                                "startDateTime": "2025-10-17T10:00:00",
                                                "endDateTime": "2025-10-17T12:00:00",
                                                "status": "PENDING"
                                            },
                                            {
                                                "id": 3,
                                                "departureId": 1,
                                                "destinationId": 2,
                                                "startDateTime": "2025-10-17T10:00:00",
                                                "endDateTime": "2025-10-17T12:00:00",
                                                "status": "PENDING"
                                            },
                                            {
                                                "id": 2,
                                                "departureId": 1,
                                                "destinationId": 2,
                                                "startDateTime": "2025-10-17T10:00:00",
                                                "endDateTime": "2025-10-17T12:00:00",
                                                "status": "PENDING"
                                            }
                                        ],
                                        "summary": {
                                            "totalCount": 5,
                                            "approvedCount": 0,
                                            "cancelledCount": 0,
                                            "pendingCount": 5
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

        @Parameter(example = "MTc1OTY1ODA1MDcwMzo1", description = "페이지네이션 커서 값")
        @RequestParam(required = false)
        cursor: String?,
    ): ResponseEntity<ApiResponse<QueryResponse>>


    @Operation(
        summary = "노선 요청 취소",
        description = "특정 노선 요청을 취소합니다."
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