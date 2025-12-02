package com.moyeobus.infra.external.auth.security


import com.moyeobus.infra.persistence.user.entity.PassengerEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

class CustomUserDetails(
    private val passenger: PassengerEntity
) : UserDetails {

    val email: String
        get() = passenger.email

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_VIEWER"))
    }

    override fun getPassword(): String = passenger.password?: ""

    override fun getUsername(): String = passenger.email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}