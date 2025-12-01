package com.moyeobus.infra.persistence.user.entity

import com.moyeobus.infra.external.oauth2.user.OAuth2Provider
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
    var autoLoginAgreed: Boolean,

    @Enumerated(EnumType.STRING)
    var userType: UserType
)

enum class UserType { KAKAO, GOOGLE, GUEST;

    companion object {
        fun from(provider: OAuth2Provider): UserType {
            return when (provider) {
                OAuth2Provider.GOOGLE -> GOOGLE
                OAuth2Provider.KAKAO -> KAKAO
            }
        }
    }
}