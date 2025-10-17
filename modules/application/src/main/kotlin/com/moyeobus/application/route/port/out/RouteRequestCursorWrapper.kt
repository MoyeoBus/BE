package com.moyeobus.application.route.port.out

import java.time.Instant

data class RouteRequestCursorWrapper(
    val cursorCreatedAt: Instant?,
    val cursorId: Long?
)
