package com.moyeobus.api.docs

import com.moyeobus.application.survey.port.`in`.SurveyCommand
import com.moyeobus.application.survey.port.`in`.SurveyOptionResult
import com.moyeobus.global.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(
    name = "설문 API",
    description = """
    📝 설문 조사 API 그룹
    """
)
interface SurveyControllerDocs {

    @Operation(
        summary = "설문 옵션 조회",
        description = "사용자가 선택할 수 있는 모든 설문 항목을 조회합니다."
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
                                        "id": 1,
                                        "reason": "대중교통이 너무 적어서",
                                        "active": true
                                      },
                                      {
                                        "id": 2,
                                        "reason": "기존 버스 시간이 맞지 않아서",
                                        "active": true
                                      },
                                      {
                                        "id": 3,
                                        "reason": "환승이 너무 불편해서",
                                        "active": true
                                      },
                                      {
                                        "id": 4,
                                        "reason": "택시비가 부담돼서",
                                        "active": true
                                      },
                                      {
                                        "id": 5,
                                        "reason": "이동에 도움이 필요해서",
                                        "active": true
                                      },
                                      {
                                        "id": 6,
                                        "reason": "병원, 학교 등 필수 목적지 이동이 필요해서",
                                        "active": true
                                      },
                                      {
                                        "id": 7,
                                        "reason": "출근/통학 시간대 이동 수단이 없어서",
                                        "active": true
                                      },
                                      {
                                        "id": 8,
                                        "reason": "기타",
                                        "active": true
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
    fun queryAll(): ResponseEntity<ApiResponse<SurveyOptionResult>>


    @Operation(
        summary = "설문 응답 생성",
        description = "사용자의 설문 응답(`optionId`,`departureId`,`destinationId`)을 저장합니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "201",
                description = "설문 응답 저장 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "응답 저장 성공",
                                value = """
                                {
                                  "code": "COMMON_201",
                                  "message": "데이터가 정상적으로 생성되었습니다.",
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
                description = "요청 데이터가 올바르지 않음",
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
                                  "result": "설문 항목 ID는 필수입니다.",
                                  "isSuccess": false
                                }
                                """
                            )
                        ]
                    )
                ]
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "요청 데이터 중 잘못된 값이 있음.",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "잘못된 요청 예시 1 - 설문조사 선택지의 ID 값이 DB에 없는 값일 경우 ",
                                value = """
                                {
                                    "code": "COMMON_500",
                                    "message": "서버 에러, 관리자에게 문의 바랍니다.",
                                    "result": "Contain invalid option id",
                                    "isSuccess": false,
                                    "path": "/api/v1/surveys"
                                }
                                """
                            ),
                            ExampleObject(
                                name = "잘못된 요청 예시 2 - 출발지나 도착지의 ID 값이 DB에 없는 값일 경우",
                                value = """
                                {
                                    "code": "COMMON_500",
                                    "message": "서버 에러, 관리자에게 문의 바랍니다.",
                                    "result": "Contain invalid spot id",
                                    "isSuccess": false,
                                    "path": "/api/v1/surveys"
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
            description = "사용자가 선택한 설문 응답 정보",
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "설문 응답 요청 예시",
                            value = """
                            {
                                "optionId" : 1,
                                "departureId" : 2,
                                "destinationId" : 3
                            }
                            """
                        )
                    ]
                )
            ]
        )
        @RequestBody command: SurveyCommand
    ): ResponseEntity<ApiResponse<Void>>
}