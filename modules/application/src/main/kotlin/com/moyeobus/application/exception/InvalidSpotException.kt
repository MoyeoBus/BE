package com.moyeobus.application.exception

import com.moyeobus.global.response.exception.GlobalException
import com.moyeobus.global.response.status.ErrorStatus

class InvalidSpotException(id: Long) : GlobalException(ErrorStatus.INVALID_SURVEY_SPOT.withDetail(id.toString()))