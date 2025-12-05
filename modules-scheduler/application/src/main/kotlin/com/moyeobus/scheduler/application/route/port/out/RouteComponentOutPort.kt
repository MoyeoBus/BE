package com.moyeobus.scheduler.application.route.port.out

import com.moyeobus.scheduler.domain.route.RouteComponent

interface RouteComponentOutPort {
    fun save(component: RouteComponent)
    fun saveAll(components: List<RouteComponent>)
}