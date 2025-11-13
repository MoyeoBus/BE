package com.moyeobus.infra.persistence.route.entity

import com.moyeobus.global.entity.BaseEntity
import com.moyeobus.infra.persistence.address.entity.AddressEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "route_request")
class RouteRequestEntity (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val passengerId: Long,

    var routeId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_id")
    val departure: AddressEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id")
    val destination: AddressEntity,

    val startDateTime: Instant,

    val endDateTime: Instant,

    val status: String
): BaseEntity()