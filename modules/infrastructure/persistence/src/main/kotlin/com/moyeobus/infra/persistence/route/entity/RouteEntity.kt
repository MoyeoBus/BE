package com.moyeobus.infra.persistence.route.entity

import com.moyeobus.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "route")
class RouteEntity (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val operatorId: Long,

    val localGovId: Long
) : BaseEntity()