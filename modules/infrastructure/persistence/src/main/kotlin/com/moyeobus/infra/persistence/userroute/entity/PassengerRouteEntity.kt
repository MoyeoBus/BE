package com.moyeobus.infra.persistence.userroute.entity

import com.moyeobus.global.entity.BaseEntity
import com.moyeobus.infra.persistence.route.entity.RouteEntity
import com.moyeobus.infra.persistence.user.entity.PassengerEntity
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