package com.moyeobus.application.route.port.`in`



interface RouteUseCase {
    fun request(command: RouteCommand)
    fun query(): RouteRequestResult
    fun cancel(requestId: Long)
}