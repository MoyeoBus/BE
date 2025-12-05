package com.moyeobus.scheduler.infra.persistence.passenger.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "passenger")
class PassengerEntity (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var email: String,
    var password: String?,

    var autoLoginAgreed: Boolean,

    @Enumerated(EnumType.STRING)
    var userType: UserType
)

enum class UserType { KAKAO, GOOGLE, LOCAL }