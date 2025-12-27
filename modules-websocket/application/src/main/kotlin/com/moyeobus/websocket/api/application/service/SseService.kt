package com.moyeobus.websocket.api.application.service

import com.moyeobus.websocket.api.application.port.`in`.SocketUseCase
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap

@Service
class SseService : SocketUseCase{
    private val emitters = ConcurrentHashMap<Long, SseEmitter>()

    override fun subscribe(routeId: Long) : SseEmitter {
        val emitter = SseEmitter(Long.MAX_VALUE)

        emitters[routeId] = emitter

        // 연결 종료 시 정리
        emitter.onCompletion {
            emitters.remove(routeId)
        }

        emitter.onTimeout {
            emitters.remove(routeId)
        }

        emitter.onError {
            emitters.remove(routeId)
        }

        // 최초 연결 확인용 이벤트 (권장)
        try {
            emitter.send(
                SseEmitter.event()
                    .name("connect")
                    .data("connected")
            )
        } catch (e: Exception) {
            emitters.remove(routeId)
        }

        return emitter
    }
    /**
     * 특정 사용자에게 이벤트 전송
     */
    @Async
    override fun sendToUser(routeId: Long, data: Any) {
        val emitter = emitters[routeId] ?: return

        try {
            emitter.send(
                SseEmitter.event()
                    .name("notification")
                    .data(data)
            )
        } catch (e: Exception) {
            emitters.remove(routeId)
        }
    }

    /**
     * 전체 사용자에게 브로드캐스트
     */
    @Async
    override fun broadcast(data: Any) {
        emitters.forEach { (routeId, emitter) ->
            try {
                emitter.send(
                    SseEmitter.event()
                        .name("broadcast")
                        .data(data)
                )
            } catch (e: Exception) {
                emitters.remove(routeId)
            }
        }
    }
}