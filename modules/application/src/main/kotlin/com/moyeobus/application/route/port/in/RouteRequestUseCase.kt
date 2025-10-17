package com.moyeobus.application.route.port.`in`


interface RouteRequestUseCase {
    fun request(command: RouteCommand)
    fun cancel(requestId: Long)
}