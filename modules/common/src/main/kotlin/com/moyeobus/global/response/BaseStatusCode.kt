package com.moyeobus.global.response

import org.springframework.http.HttpStatus

interface BaseStatusCode {
    val httpStatus: HttpStatus?
    val code: String?
    val message: String?
}
