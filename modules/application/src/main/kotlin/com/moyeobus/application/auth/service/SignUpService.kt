package com.moyeobus.application.auth.service

import com.moyeobus.application.auth.port.`in`.SignUpCommand
import com.moyeobus.application.auth.port.`in`.SignUpUseCase
import com.moyeobus.application.exception.EmailExistException
import com.moyeobus.application.user.port.out.PassengerOutPort
import com.moyeobus.domain.user.Passenger
import com.moyeobus.domain.user.UserType
import org.springframework.stereotype.Service

@Service
class SignUpService(
    private val passengerRepository: PassengerOutPort,
) : SignUpUseCase {
    override fun processSignUp(request: SignUpCommand) {
        if (checkEmailAvailable(request.email)) {
           val new = Passenger(
               id = null,
               name = request.name,
               email = request.email,
               password = passengerRepository.encode(request.password),
               autoLoginAgreed = false,
               userType = UserType.LOCAL
           )
            passengerRepository.save(new)
        }
    }
    private fun checkEmailAvailable(email: String): Boolean {
        if (!passengerRepository.existsByEmail(email)) {
            return true
        } else {
            throw EmailExistException()
        }
    }

}