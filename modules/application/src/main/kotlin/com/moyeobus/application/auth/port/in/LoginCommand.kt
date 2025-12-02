package com.moyeobus.application.auth.port.`in`

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginCommand(
    @field:Email(message = "이메일 형식이 아닙니다")
    @field:NotBlank(message = "이메일은 필수입니다")
    val email: String = "",
    @field:NotBlank(message = "비밀번호는 필수입니다")
    @field:Size(min = 7, message = "비밀번호 길이는 최소 7 이상이어야 합니다.")
    val password: String = ""
)