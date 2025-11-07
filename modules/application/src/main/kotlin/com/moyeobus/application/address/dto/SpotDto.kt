package com.moyeobus.application.address.dto

data class SpotDto(
    val id: Long,
    val name: String,
    val lat: Double,
    val lon: Double,
    val postCode: String
)
