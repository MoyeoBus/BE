package com.moyeobus.infra.persistence.route.entity

import com.moyeobus.infra.persistence.address.entity.AddressEntity
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
@Table(name = "route_component")
class RouteComponentEntity (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    val route: RouteEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    val spot: AddressEntity,

    val assignedTime: Instant
)