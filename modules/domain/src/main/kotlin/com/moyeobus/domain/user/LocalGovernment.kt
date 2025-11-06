package com.moyeobus.domain.user

import com.moyeobus.domain.route.Area

data class LocalGovernment(
    var id: Long? = null,

    val area: Area,

    var loginId: String,

    var govName: String
)
