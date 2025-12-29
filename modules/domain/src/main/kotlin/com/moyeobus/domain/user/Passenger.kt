package com.moyeobus.domain.user

data class Passenger(
    var id: Long? = null,

    var name: String,

    var email: String,

    var password: String?,

    var autoLoginAgreed: Boolean,

    var userType: UserType
)

enum class UserType { KAKAO, GOOGLE, LOCAL }