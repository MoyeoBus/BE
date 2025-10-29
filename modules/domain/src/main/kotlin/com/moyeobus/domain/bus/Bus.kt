package com.moyeobus.domain.bus

data class Bus (
    private val id: Long? = null,

    private val operatorId: Long,

    private val busNumber: Int,

    private val carNumber: String,

    private val isOperating: Boolean
)