package com.moyeobus.application.auth.port.`in`

interface SignUpUseCase {
    fun processSignUp(request: SignUpCommand)
}