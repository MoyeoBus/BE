package com.moyeobus.scheduler.application.event

import com.moyeobus.scheduler.domain.route.Route

interface EventOutport {
    fun sendEvent(topic: String, route: Route)
}