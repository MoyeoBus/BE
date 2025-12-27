package com.moyeobus.api.docs

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(
    name = "로그아웃 API",
    description = """
    👋 로그아웃 API
    """
)
interface LogoutControllerDocs {
    @Operation(
        summary = "로그아웃",
        description = "로그아웃을 하면 토큰이 담긴 쿠키가 비워지며, 리프레쉬 토큰이 블랙리스트에 올라 탈취 시 사용을 금합니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "204",
                description = "로그아웃 성공",
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
            )
        ]
    )
    fun logout()
}