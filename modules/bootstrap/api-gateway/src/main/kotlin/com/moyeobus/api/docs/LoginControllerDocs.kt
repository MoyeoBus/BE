package com.moyeobus.api.docs

import com.moyeobus.application.auth.port.`in`.LoginCommand
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse

@Tag(
    name = "로그인 API",
    description = """
    🪪 자체 로그인 및 OAuth 로그인 API 그룹
    """
)
interface LoginControllerDocs {

    @Operation(
        summary = "일반 로그인",
        description = "이메일과 비밀번호를 통해 로그인을 수행합니다. <br/>"
                + "성공 시 Access · Refresh 토큰을 자동 발급하며, 모두 쿠키로 전달됩니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "로그인 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "성공 예시",
                                value = """
                                { }
                                """
                            )
                        ]
                    )
                ]
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "로그인 실패 (비밀번호 불일치 / 존재하지 않는 사용자)",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "비밀번호 불일치",
                                value = """
                                {
                                    "code": "COMMON_401",
                                    "message": "인증이 필요합니다.",
                                    "path": "/api/v1/login",
                                    "isSuccess": false
                                }
                                """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    fun localLogin(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "로그인 요청 JSON",
            required = true,
            content = [
                Content(
                    examples = [
                        ExampleObject(
                            name = "로그인 요청 예시",
                            value = """
                            {
                              "email": "test@example.com",
                              "password": "12345678"
                            }
                            """
                        )
                    ]
                )
            ]
        )
        request: LoginCommand
    )


    @Operation(
        summary = "소셜 로그인 리다이렉트",
        description = "OAuth2 기반 소셜 로그인으로 이동하기 위한 리다이렉트 엔드포인트입니다.<br/><br/>"
                + "**지원 제공자(provider)**: `google`, `kakao` 등<br/><br/>"
                + "요청 시 클라이언트는 `/oauth2/authorization/{provider}` 로 Redirect 됩니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "302",
                description = "리다이렉트 성공"
            )
        ]
    )
    fun redirectToProvider(
        @Parameter(
            description = "OAuth 로그인 제공자",
            example = "google",
            required = true
        )
        provider: String,

        @Parameter(hidden = true)
        response: HttpServletResponse
    )
}