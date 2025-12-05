package com.moyeobus.scheduler.infra.persistence.routeowner.entity

import com.moyeobus.scheduler.global.entity.BaseEntity
import com.moyeobus.scheduler.infra.persistence.passenger.entity.PassengerEntity
import com.moyeobus.scheduler.infra.persistence.route.entity.RouteEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "passenger_route")
class PassengerRouteEntity (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    val passengerEntity: PassengerEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    val route: RouteEntity
): BaseEntity()