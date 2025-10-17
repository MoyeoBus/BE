package com.moyeobus.global.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.moyeobus.global.response.status.SuccessStatus

@JsonPropertyOrder("code", "message", "result", "isSuccess")
data class ApiResponse<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val result: T?
) {


    companion object {
        // 성공한 경우 응답 생성
        fun <T> onSuccess(result: T): ApiResponse<T> {
            return ApiResponse(true, SuccessStatus.OK.code, SuccessStatus.OK.message, result)
        }

        fun <T> onSuccessVoid(): ApiResponse<T> {
            return ApiResponse(true, SuccessStatus.OK.code, SuccessStatus.OK.message, null)
        }

        // 실패한 경우 응답 생성
        fun <T> onFailure(code: String, message: String, data: T): ApiResponse<T> {
            return ApiResponse(false, code, message, data)
        }
    }
}
