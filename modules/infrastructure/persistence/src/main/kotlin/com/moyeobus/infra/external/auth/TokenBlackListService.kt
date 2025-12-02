package com.moyeobus.infra.external.auth

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class TokenBlackListService(
    private val redisTemplate: StringRedisTemplate
) {

    fun addRefreshTokenBlackList(refreshToken: String, expiration: Long) {
        redisTemplate.opsForValue().set(
            "blacklist:refresh:$refreshToken",
            "1",
            Duration.ofSeconds(expiration)
        )
    }

    fun isAlreadyBlackListed(refreshToken: String): Boolean {
        val value = redisTemplate.opsForValue().get("blacklist:refresh:$refreshToken")
        return value != null
    }
}