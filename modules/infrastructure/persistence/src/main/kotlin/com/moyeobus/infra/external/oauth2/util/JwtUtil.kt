package com.moyeobus.infra.external.oauth2.util

import com.moyeobus.infra.exception.InvalidTokenException
import com.moyeobus.infra.persistence.passenger.repository.PassengerJpaRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant

import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


@Component
class JwtUtil(
    @Value("\${jwt.secret}") secret: String,
    private val passengerRepository: PassengerJpaRepository,
) {
    private val secretKey: SecretKey = SecretKeySpec(
        secret.toByteArray(StandardCharsets.UTF_8),
        Jwts.SIG.HS256.key().build().algorithm
    )

    fun getEmail(token: String?): String {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get<String>("email", String::class.java)
    }

    fun getCategory(token: String?): String? {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
            .get<String?>("category", String::class.java)
    }

    fun isExpired(token: String?): Boolean {
        try {
            val claims = extractClaims(token)
            val expiration = claims.getExpiration()

            if (expiration == null) {
                throw InvalidTokenException()
            }

            return expiration.before(Date())
        } catch (e: ExpiredJwtException) {
            return true
        } catch (e: Exception) {
            throw InvalidTokenException()
        }
    }

    fun createAccess(email: String?): String {
        return Jwts.builder()
            .claim("category", "access")
            .claim("email", email)
            .claim("role", "ROLE_MANAGER")
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
            .signWith(secretKey)
            .compact()
    }

    fun createRefresh(email: String?): String {
        return Jwts.builder()
            .claim("category", "refresh")
            .claim("email", email)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
            .signWith(secretKey)
            .compact()
    }

    private fun extractClaims(token: String?): Claims {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseSignedClaims(token)
            .getBody()
    }

    fun getAuthentication(token: String?): Authentication {
        val email = getEmail(token)
        require(!(email == null || email.isEmpty())) { "JWT token does not contain a valid googleId." }

        val passenger = passengerRepository.findByEmail(email)
            ?:throw UsernameNotFoundException(email)



        val authorities: MutableList<GrantedAuthority?> = ArrayList<GrantedAuthority?>()

        val userDetails: UserDetails = User(
            passenger.email, "", authorities
        )

        return UsernamePasswordAuthenticationToken(userDetails, "", authorities)
    }

    fun reIssueToken(refreshToken: String): String? {
        val token = refreshToken.substring(7)
        if (!isExpired(token)) {
            return createAccess(getEmail(token))
        }
        throw InvalidTokenException()
    }

    fun getRefreshTokenExpireTime(refreshToken: String): Long =
        runCatching {
            val claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(refreshToken)
                .body

            val expiration = claims.expiration ?: return 0L
            val now = Instant.now()
            val exp = expiration.toInstant()

            Duration.between(now, exp).seconds.coerceAtLeast(0)
        }.getOrElse { 0L }

    companion object {
        private const val MINUTE = 60 * 1000L
        private const val HOUR = 60 * MINUTE

        // TODO: 동작 확인 이후 각각 15분, 1시간으로 변경
        const val ACCESS_TOKEN_EXPIRE_TIME = 1 * MINUTE
        const val REFRESH_TOKEN_EXPIRE_TIME = 5 * MINUTE
    }
}
