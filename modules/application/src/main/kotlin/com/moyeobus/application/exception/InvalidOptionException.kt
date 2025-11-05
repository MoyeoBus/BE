package com.moyeobus.application.exception

import com.moyeobus.global.response.exception.GlobalException
import com.moyeobus.global.response.status.ErrorStatus

class InvalidOptionException(id: Long) : GlobalException(
    ErrorStatus.NOT_FOUND.withDetail("설문 옵션(id=$id)을 찾을 수 없습니다.")
)