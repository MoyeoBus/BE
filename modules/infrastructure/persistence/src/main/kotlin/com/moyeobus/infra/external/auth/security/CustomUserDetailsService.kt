package com.moyeobus.infra.external.auth.security;

import com.moyeobus.infra.persistence.user.repository.PassengerJpaRepository
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
class CustomUserDetailsService(
        private val passengerRepository: PassengerJpaRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val passenger = passengerRepository.findByEmail(email)
                ?: throw UsernameNotFoundException("Passenger not found with email: $email")

        return CustomUserDetails(passenger)
    }
}
