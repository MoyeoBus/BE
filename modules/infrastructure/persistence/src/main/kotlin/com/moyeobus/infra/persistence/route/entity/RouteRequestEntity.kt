package com.moyeobus.infra.persistence.route.entity

import com.moyeobus.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "route_request")
class RouteRequestEntity (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val passengerId: Long,

    val departureId: Long,

    val destinationId: Long,

    val startDateTime: Instant,

    val endDateTime: Instant,

    val status: String
): BaseEntity()