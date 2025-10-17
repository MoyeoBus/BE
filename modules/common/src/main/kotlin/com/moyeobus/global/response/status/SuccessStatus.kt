package com.moyeobus.global.response.status

import com.moyeobus.global.response.BaseStatusCode
import org.springframework.http.HttpStatus

enum class SuccessStatus(
    override val httpStatus: HttpStatus,
    override val code: String,
    override val message: String
) : BaseStatusCode {
    OK(HttpStatus.OK, "COMMON_200", "성공입니다")
}