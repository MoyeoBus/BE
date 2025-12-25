package com.moyeobus.websocket.application.service

import com.moyeobus.websocket.application.port.`in`.SocketUseCase
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap

@Service
class SseService : SocketUseCase{
    private val emitters = ConcurrentHashMap<Long, SseEmitter>()

    override fun subscribe(userId: Long) : SseEmitter {
        val emitter = SseEmitter(60_000L)

        emitters[userId] = emitter

        // 연결 종료 시 정리
        emitter.onCompletion {
            emitters.remove(userId)
        }

        emitter.onTimeout {
            emitters.remove(userId)
        }

        emitter.onError {
            emitters.remove(userId)
        }

        // 최초 연결 확인용 이벤트 (권장)
        try {
            emitter.send(
                SseEmitter.event()
                    .name("connect")
                    .data("connected")
            )
        } catch (e: Exception) {
            emitters.remove(userId)
        }

        return emitter
    }
    /**
     * 특정 사용자에게 이벤트 전송
     */
    @Async
    override fun sendToUser(userId: Long, data: Any) {
        val emitter = emitters[userId] ?: return

        try {
            emitter.send(
                SseEmitter.event()
                    .name("notification")
                    .data(data)
            )
        } catch (e: Exception) {
            emitters.remove(userId)
        }
    }

    /**
     * 전체 사용자에게 브로드캐스트
     */
    @Async
    override fun broadcast(data: Any) {
        emitters.forEach { (userId, emitter) ->
            try {
                emitter.send(
                    SseEmitter.event()
                        .name("broadcast")
                        .data(data)
                )
            } catch (e: Exception) {
                emitters.remove(userId)
            }
        }
    }
}