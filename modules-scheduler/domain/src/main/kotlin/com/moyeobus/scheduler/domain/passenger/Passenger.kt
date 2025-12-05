package com.moyeobus.scheduler.domain.passenger

data class Passenger(
    var id: Long? = null,

    var email: String,

    var password: String?,

    var autoLoginAgreed: Boolean,

    var userType: UserType
)

enum class UserType { KAKAO, GOOGLE, LOCAL }
