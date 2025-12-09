package com.moyeobus.application.route.port.`in`


interface RouteRequestUseCase {
    fun request(passengerId: Long, command: RouteCommand)
    fun cancel(requestId: Long)
}