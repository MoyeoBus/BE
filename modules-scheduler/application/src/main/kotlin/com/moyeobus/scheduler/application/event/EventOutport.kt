package com.moyeobus.scheduler.application.event

interface EventOutport {
    fun sendEvent(topic: String, routeId: Long)
}