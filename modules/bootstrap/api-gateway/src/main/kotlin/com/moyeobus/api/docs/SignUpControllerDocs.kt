package com.moyeobus.api.docs

import com.moyeobus.application.auth.port.`in`.SignUpCommand
import com.moyeobus.global.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(
    name = "회원가입 API",
    description = """
    📇 회원가입 API 그룹
    """
)
interface SignUpControllerDocs {

    @Operation(
        summary = "회원가입 ✅",
        description = "사용자의 이름, 이메일과 비밀번호를 입력받아 계정을 생성합니다.\n\n⚠️ 비밀번호는 최소 7자 이상이어야 합니다."
    )
    @ApiResponses(
        value = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "201",
                description = "회원가입 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "회원가입 성공",
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
                description = "잘못된 형식의 요청",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "이메일 형식 오류",
                                value = """
                                {
                                    "code": "COMMON_400",
                                    "message": "이메일 형식이 아닙니다",
                                    "isSuccess": false,
                                    "path": "/api/v1/signup"
                                }
                                """
                            ),
                            ExampleObject(
                                name = "비밀번호 길이 미충족",
                                value = """
                                {
                                    "code": "COMMON_400",
                                    "message": "비밀번호 길이는 최소 7 이상이어야 합니다.",
                                    "isSuccess": false,
                                    "path": "/api/v1/signup"
                                }
                                """
                            ),
                            ExampleObject(
                                name = "이메일 미입력",
                                value = """
                                {
                                    "code": "COMMON_400",
                                    "message": "이메일은 필수입니다",
                                    "isSuccess": false,
                                    "path": "/api/v1/signup"
                                }
                                """
                            ),
                            ExampleObject(
                                name = "비밀번호 미입력",
                                value = """
                                {
                                    "code": "COMMON_400",
                                    "message": "비밀번호는 필수입니다",
                                    "isSuccess": false,
                                    "path": "/api/v1/signup"
                                }
                                """
                            ),
                            ExampleObject(
                                name = "이름 미입력",
                                value = """
                                {
                                    "code": "COMMON_400",
                                    "message": "이름은 필수입니다",
                                    "isSuccess": false,
                                    "path": "/api/v1/signup"
                                }
                                """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    fun signUp(
        @RequestBody request: SignUpCommand
    ): ResponseEntity<ApiResponse<Void>>
}