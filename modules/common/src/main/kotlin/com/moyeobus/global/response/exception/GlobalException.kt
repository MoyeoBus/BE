package com.moyeobus.global.response.exception

import com.moyeobus.global.response.BaseStatusCode
import com.moyeobus.global.response.ErrorDetail


open class GlobalException(val code: BaseStatusCode) : RuntimeException(code.message) {
    val errorDetail: ErrorDetail
        get() = ErrorDetail.Companion.from(this.code)
}