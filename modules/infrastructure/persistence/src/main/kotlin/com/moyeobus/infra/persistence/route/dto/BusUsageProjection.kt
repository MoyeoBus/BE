package com.moyeobus.infra.persistence.route.dto

interface BusUsageProjection {
    val operateCount: Int
    val completedCount: Int
}