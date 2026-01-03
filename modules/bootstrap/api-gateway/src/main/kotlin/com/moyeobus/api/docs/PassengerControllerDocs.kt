package com.moyeobus.api.docs


import com.moyeobus.api.passenger.dto.UserInfoResponse
import com.moyeobus.infra.external.auth.security.CustomUserDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity

@Tag(
    name = "승객 API",
    description = """
    🙋‍♂️ 로그인된 승객이 자신의 정보를 조회하는 API 그룹
    """
)
interface PassengerControllerDocs {

    @Operation(
        summary = "내 정보 조회"
    )
    @ApiResponses(
        value = [

            // ✅ 200 OK
            ApiResponse(
                responseCode = "200",
                description = "내 정보 조회 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "성공 예시",
                                value = """
                                {
                                  "code": "COMMON_200",
                                  "message": "요청에 성공했습니다.",
                                  "isSuccess": true,
                                  "result": {
                                    "name": "테스트사용자1"
                                  }
                                }
                                """
                            )
                        ]
                    )
                ]
            ),

            // ❌ 401 Unauthorized
            ApiResponse(
                responseCode = "401",
                description = "인증되지 않은 사용자",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "인증 실패",
                                value = """
                                {
                                  "code": "COMMON_401",
                                  "message": "인증이 필요합니다.",
                                  "path": "/api/v1/passengers/me",
                                  "isSuccess": false
                                }
                                """
                            )
                        ]
                    )
                ]
            ),

            // ❌ 404 Not Found
            ApiResponse(
                responseCode = "404",
                description = "존재하지 않는 사용자",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "사용자 없음",
                                value = """
                                {
                                  "code": "COMMON_404",
                                  "message": "Passenger(email=illegal@illegal.com) not found",
                                  "path": "/api/v1/passengers/me",
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
    fun getMyInfo(customUserDetails: CustomUserDetails) : ResponseEntity<com.moyeobus.global.response.ApiResponse<UserInfoResponse>>
}