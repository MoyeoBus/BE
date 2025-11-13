package com.moyeobus.infra.persistence.route.dto

interface HourUseProjection {
    val hour: Int
    val useCount: Int
}