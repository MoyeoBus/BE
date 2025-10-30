package com.moyeobus.infra.persistence.bus.entity

import com.moyeobus.domain.bus.BusStatus
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "bus")
class BusEntity (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val operatorId: Long,

    val busNumber: Int,

    val carNumber: String,

    @Enumerated(EnumType.STRING)
    var status: BusStatus
)