package com.moyeobus.global.response

import org.springframework.http.HttpStatus

data class ErrorDetail(
    val httpStatus: HttpStatus?,
    val code: String?,
    val message: String?
) {
    companion object {
        fun from(code: BaseStatusCode): ErrorDetail {
            return ErrorDetail(
                code.httpStatus,
                code.code,
                code.message
            )
        }
    }
}