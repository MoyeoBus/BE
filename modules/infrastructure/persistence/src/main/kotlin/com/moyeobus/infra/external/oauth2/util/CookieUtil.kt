package com.moyeobus.infra.external.oauth2.util

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

    fun addCookie(response: HttpServletResponse, name: String?, value: String?, maxAge: Int) {
        val cookie: Cookie = Cookie(name, value)
        cookie.maxAge = maxAge
        cookie.path = "/"
        // cookie.setDomain("");
        cookie.isHttpOnly = false
        cookie.secure = true
        cookie.setAttribute("SameSite", "None")
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