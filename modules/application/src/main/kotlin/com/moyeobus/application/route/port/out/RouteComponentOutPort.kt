package com.moyeobus.application.route.port.out

import com.moyeobus.domain.route.RouteComponent

interface RouteComponentOutPort {
    fun save(component: RouteComponent)
    fun saveAll(components: List<RouteComponent>)
    fun findById(id: Long) : RouteInfoDto
}