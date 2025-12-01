package com.moyeobus.infra.external.oauth2.service

import com.moyeobus.infra.external.oauth2.exception.OAuth2AuthenticationProcessingException
import com.moyeobus.infra.external.oauth2.user.OAuth2UserInfo
import com.moyeobus.infra.external.oauth2.user.OAuth2UserInfoFactory
import com.moyeobus.infra.persistence.user.entity.PassengerEntity
import com.moyeobus.infra.persistence.user.entity.UserType
import com.moyeobus.infra.persistence.user.repository.PassengerJpaRepository
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val passengerRepository: PassengerJpaRepository
) : DefaultOAuth2UserService() {

    @kotlin.Throws(OAuth2AuthenticationProcessingException::class)
    override fun loadUser(oAuth2UserRequest: OAuth2UserRequest): OAuth2User {


        val oAuth2User: OAuth2User = super.loadUser(oAuth2UserRequest)

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User)
        } catch (ex: java.lang.Exception) {
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }
    }

    private fun processOAuth2User(userRequest: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2User {
        val registrationId = userRequest.getClientRegistration()
            .getRegistrationId()

        val accessToken = userRequest.getAccessToken().getTokenValue()


        val oAuth2UserInfo: OAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
            registrationId,
            accessToken,
            oAuth2User.getAttributes()
        )

        val oauthEmail = oAuth2UserInfo.email


        if (!org.springframework.util.StringUtils.hasText(oauthEmail)) {
            throw OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider")
        }

        val existingUser = passengerRepository.findByEmail(oauthEmail)


        if (existingUser == null) {
            val user = PassengerEntity(
                id = null,
                email = oauthEmail,
                autoLoginAgreed = false,
                userType = UserType.from(oAuth2UserInfo.provider)
            )
            passengerRepository.save(user)
        }
        return OAuth2UserPrincipal(oAuth2UserInfo)
    }
}