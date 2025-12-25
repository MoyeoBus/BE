package com.moyeobus.websocket.application.port.`in`

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

interface SocketUseCase {
    fun subscribe(userId: Long) : SseEmitter
    fun sendToUser(userId: Long, data: Any)
    fun broadcast(data: Any)
}