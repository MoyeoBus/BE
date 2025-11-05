package com.moyeobus.application.exception

import com.moyeobus.global.response.exception.GlobalException
import com.moyeobus.global.response.status.ErrorStatus

class InvalidOptionException(id: Long) : GlobalException(ErrorStatus.INVALID_SURVEY_OPTION.withDetail(id.toString()))