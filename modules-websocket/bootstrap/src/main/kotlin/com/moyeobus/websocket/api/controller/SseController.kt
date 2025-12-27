package com.moyeobus.websocket.api.controller

import com.moyeobus.websocket.api.application.port.`in`.SocketUseCase
import com.moyeobus.websocket.api.docs.SseControllerDocs
import com.moyeobus.websocket.api.dto.BusLocationDto
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/sse")
@CrossOrigin("*")
class SseController(
    private val sseService: SocketUseCase
) : SseControllerDocs {
    @GetMapping(
        "/subscribe/{routeId}",
        produces = [MediaType.TEXT_EVENT_STREAM_VALUE]
    )
    override fun subscribe(@PathVariable routeId: Long): SseEmitter {
        return sseService.subscribe(routeId)
    }

    @PostMapping("/bus/location")
    override fun updateLocation(@RequestBody dto: BusLocationDto) {
        sseService.broadcast(dto)
    }
}