package com.moyeobus.infra.persistence.route.dto

import java.time.LocalDate

interface DateUseProjection {
    val date: LocalDate
    val useCount: Int
}