package com.moyeobus.api.docs

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(
    name = "토큰 재발급 API",
    description = """
     🪪 토큰 재발급 API
    """
)
interface TokenControllerDocs {
    @Operation(
        summary = "토큰 재발급 ✅ (Http 메서드 변경; GET -> POST)",
        description = "유효기간이 남아있는 리프레쉬 토큰으로 새로운 액세스 토큰을 발급받습니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "재발급 성공",
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
                description = "리프레쉬 토큰 만료로 인한 재발급 실패",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "실패 예시",
                                value = """
                                {
                                    "code": "COMMON_401",
                                    "message": "인증이 필요합니다.",
                                    "path": "/api/v1/tokens",
                                    "success": false
                                }
                                """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    fun reissue()
}