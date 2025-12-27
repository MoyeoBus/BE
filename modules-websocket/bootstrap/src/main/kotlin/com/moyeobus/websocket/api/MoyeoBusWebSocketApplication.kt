package com.moyeobus.websocket.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.moyeobus.websocket"])
class MoyeoBusWebSocketApplication

fun main(args: Array<String>) {
    runApplication<MoyeoBusWebSocketApplication>(*args)
}