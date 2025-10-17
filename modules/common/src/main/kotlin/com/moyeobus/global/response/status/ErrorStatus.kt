package com.moyeobus.global.response.status

import com.moyeobus.global.response.BaseStatusCode
import org.springframework.http.HttpStatus


enum class ErrorStatus(
    override val httpStatus: HttpStatus,
    override val code: String,
    override val message: String
) : BaseStatusCode {
    // 일반 응답
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_500", "서버 에러, 관리자에게 문의 바랍니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON_401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON_403", "금지된 요청입니다."),

    // JWT
    EMPTY_JWT(HttpStatus.UNAUTHORIZED, "COMMON_404", "토큰이 비어있습니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "COMMON_405", "유효하지 않은 토큰입니다."),

    // USER
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER_001", "이미 존재하는 사용자이며, 비밀번호가 틀렸습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER_002", "해당 사용자를 찾을 수 없습니다.");
}
