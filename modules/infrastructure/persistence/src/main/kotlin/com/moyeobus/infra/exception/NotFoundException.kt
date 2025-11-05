package com.moyeobus.infra.exception

import com.moyeobus.global.response.exception.GlobalException
import com.moyeobus.global.response.status.ErrorStatus

class NotFoundException(detail: String) : GlobalException(ErrorStatus.NOT_FOUND.withDetail(detail))