package com.moyeobus.global.response.status

import com.moyeobus.global.response.BaseStatusCode
import org.springframework.http.HttpStatus

enum class SuccessStatus(
    override val httpStatus: HttpStatus,
    override val code: String,
    override val message: String
) : BaseStatusCode {
    OK(HttpStatus.OK, "COMMON_200", "요청이 정상적으로 처리되었습니다."),
    CREATED(HttpStatus.CREATED, "COMMON_201", "데이터가 정상적으로 생성되었습니다.")
}