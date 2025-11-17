package com.moyeobus.api.localgov.dto

import com.moyeobus.application.localgov.port.`in`.OperationHistory
import com.moyeobus.application.route.model.BusUsageCount

data class OperationResult(
    val operationCount: Int,
    val busUsage: BusUsageCount,
    val history: List<OperationHistory>
)