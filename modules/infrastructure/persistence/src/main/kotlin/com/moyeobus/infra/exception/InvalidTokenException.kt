package com.moyeobus.infra.exception

import com.moyeobus.global.response.exception.GlobalException
import com.moyeobus.global.response.status.ErrorStatus

class InvalidTokenException : GlobalException(ErrorStatus.INVALID_JWT)