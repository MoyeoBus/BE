package com.moyeobus.application.exception

import com.moyeobus.global.response.exception.GlobalException
import com.moyeobus.global.response.status.ErrorStatus

class InvalidSpotException(id: Long) : GlobalException(
    ErrorStatus.NOT_FOUND.withDetail("정류장(id=$id)을 찾을 수 없습니다.")
)