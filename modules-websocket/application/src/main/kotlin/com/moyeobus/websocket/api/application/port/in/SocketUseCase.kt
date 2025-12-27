package com.moyeobus.websocket.api.application.port.`in`

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

interface SocketUseCase {
    fun subscribe(routeId: Long) : SseEmitter
    fun sendToUser(routeId: Long, data: Any)
    fun broadcast(data: Any)
}