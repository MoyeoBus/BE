package com.moyeobus.scheduler.infra.persistence.exception

import com.moyeobus.scheduler.global.response.exception.GlobalException
import com.moyeobus.scheduler.global.response.status.ErrorStatus

class NotFoundException(detail: String) : GlobalException(ErrorStatus.NOT_FOUND.withDetail(detail))