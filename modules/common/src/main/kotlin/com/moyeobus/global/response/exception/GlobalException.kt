package com.moyeobus.global.response.exception

import com.moyeobus.global.response.BaseStatusCode
import com.moyeobus.global.response.ErrorDetail


data class GlobalException(private val code: BaseStatusCode) : RuntimeException(code.message) {
    val errorDetail: ErrorDetail
        get() = ErrorDetail.Companion.from(this.code)
}