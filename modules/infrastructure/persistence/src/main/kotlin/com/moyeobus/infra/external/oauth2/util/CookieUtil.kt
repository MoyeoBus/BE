package com.moyeobus.infra.external.oauth2.util

import com.moyeobus.infra.external.oauth2.util.JwtUtil.Companion.ACCESS_TOKEN_EXPIRE_TIME
import com.moyeobus.infra.external.oauth2.util.JwtUtil.Companion.REFRESH_TOKEN_EXPIRE_TIME
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.util.SerializationUtils
import java.util.*
import java.util.Optional.empty
import java.util.Optional.of

@Component
object CookieUtil {

    fun getCookie(request: HttpServletRequest, name: String?): Optional<Cookie> {
        val cookies: Array<Cookie>? = request.cookies
        if (cookies != null) {
            for (cookie in cookies) {
                if (cookie.name.equals(name)) return of<Cookie?>(cookie)
            }
        }
        return empty<Cookie?>()
    }

    fun createAccessCookie(value: String): Cookie {
        return Cookie("access", value).apply {
            path = "/"
            maxAge = (ACCESS_TOKEN_EXPIRE_TIME / 1000).toInt()
            domain = "moyeobus.com"
            isHttpOnly = true
            secure = true
            setAttribute("SameSite", "Strict")
        }
    }

    fun createRefreshCookie(value: String): Cookie {
        return Cookie("refresh", value).apply {
            path = "/"
            maxAge = (REFRESH_TOKEN_EXPIRE_TIME / 1000).toInt()
            domain = "moyeobus.com"
            isHttpOnly = true
            secure = true
            setAttribute("SameSite", "Strict")
        }
    }

    fun addCookie(response: HttpServletResponse, name: String?, value: String?, maxAge: Int) {
        val cookie: Cookie = Cookie(name, value)
        cookie.maxAge = maxAge
        cookie.path = "/"
        // cookie.setDomain("");
        cookie.isHttpOnly = false
        cookie.secure = true
        cookie.setAttribute("SameSite", "Strict")
        response.addCookie(cookie)
    }

    fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String?) {
        val cookies: Array<Cookie>? = request.cookies
        if (cookies != null) {
            for (cookie in cookies) {
                if (cookie.name.equals(name)) {
                    cookie.value = ""
                    cookie.path = "/"
                    cookie.maxAge = 0
                    response.addCookie(cookie)
                }
            }
        }
    }

    fun expireCookie(
        response: HttpServletResponse,
        name: String,
        path: String,
        httpOnly: Boolean,
        secure: Boolean,
        sameSite: String
    ) {
        val cookie = Cookie(name, "").apply {
            this.path = path
            this.isHttpOnly = httpOnly
            this.secure = secure
            this.maxAge = 0
            this.setAttribute("SameSite", sameSite)
        }
        response.addCookie(cookie)
    }

    fun getAccessTokenFromRequest(request: HttpServletRequest): String? =
        request.cookies
            ?.firstOrNull { it.name == "access" }
            ?.value

    fun getRefreshTokenFromRequest(request: HttpServletRequest): String? =
        request.cookies
            ?.firstOrNull { it.name == "refresh" }
            ?.value

    fun serialize(`object`: Any?): String? {
        return Base64.getUrlEncoder()
            .encodeToString(SerializationUtils.serialize(`object`))
    }

    fun <T> deserialize(cookie: Cookie, cls: Class<T>): T {
        return cls.cast(
            SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.value)
            )
        )
    }
}