package com.moyeobus.infra.persistence.route.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant


@Entity
@Table(name = "route_component")
class RouteComponentEntity (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val routeId: Long,

    val name: String,

    val lat: Double,

    val lon: Double,

    val assignedTime: Instant,

    val distance: Int,

    val duration: Int,

    val isRequested: Boolean
)