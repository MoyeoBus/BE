package com.moyeobus.domain.route

data class Address (
    val id: Long? = null,

    val area: Long,

    val name: String,

    val lat: Double,

    val lon: Double,

    val postCode: String
)