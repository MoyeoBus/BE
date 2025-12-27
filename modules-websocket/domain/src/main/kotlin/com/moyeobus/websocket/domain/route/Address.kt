package com.moyeobus.websocket.domain.route

data class Address (
    val id: Long? = null,

    val area: Area,

    val name: String,

    val lat: Double,

    val lon: Double,

    val postCode: String
)