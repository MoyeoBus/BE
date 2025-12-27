package com.moyeobus.websocket.controller

import com.moyeobus.websocket.application.port.`in`.SocketUseCase
import com.moyeobus.websocket.dto.BusLocationDto
import org.springframework.http.MediaType
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/sse")
@CrossOrigin("http://localhost:63342")
class SseController(
    private val sseService: SocketUseCase
) {
    @GetMapping(
        "/subscribe",
        produces = [MediaType.TEXT_EVENT_STREAM_VALUE]
    )
    fun subscribe(@RequestParam userId: Long): SseEmitter {
        return sseService.subscribe(userId)
    }

    @PostMapping("/bus/location")
    fun updateLocation(@RequestBody dto: BusLocationDto) {
        sseService.broadcast(dto)
    }
}