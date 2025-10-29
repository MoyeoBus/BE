package com.moyeobus.infra.persistence.bus.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "bus")
class BusEntity (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null,

    private val operatorId: Long,

    private val busNumber: Int,

    private val carNumber: String,

    private val isOperating: Boolean
)