package com.moyeobus.domain.route

data class Route (

    val id: Long? = null,

    val operatorId: Long,

    val localGovId: Long,

    var busId: Long? = null,

    val routeDistance: Int,

    val routeTotalTime: Int,

    var status: RouteStatus
) {
    fun persistBus(busId: Long) {
        this.busId = busId
    }
}

enum class RouteStatus {CREATED, OPERATING, OPERATED}