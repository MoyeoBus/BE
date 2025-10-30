package com.moyeobus.domain.route

data class Route (

    val id: Long? = null,

    val operatorId: Long,

    val localGovId: Long,

    var busId: Long? = null,

    val routeDistance: Double,

    val routeTotalTime: Double,

    val routeComponents: List<RouteComponent> = emptyList()
) {
    fun persistBus(busId: Long) {
        this.busId = busId
    }
}