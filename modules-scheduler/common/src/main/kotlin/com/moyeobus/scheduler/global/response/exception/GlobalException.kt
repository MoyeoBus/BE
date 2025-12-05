package com.moyeobus.scheduler.global.response.exception

import com.moyeobus.scheduler.global.response.BaseStatusCode
import com.moyeobus.scheduler.global.response.ErrorDetail


open class GlobalException(val code: BaseStatusCode) : RuntimeException(code.message) {
    val errorDetail: ErrorDetail
        get() = ErrorDetail.Companion.from(this.code)
}