package com.moyeobus.application.exception

import com.moyeobus.global.response.exception.GlobalException
import com.moyeobus.global.response.status.ErrorStatus

class EmailExistException : GlobalException(ErrorStatus.EMAIL_ALREADY_EXISTS)