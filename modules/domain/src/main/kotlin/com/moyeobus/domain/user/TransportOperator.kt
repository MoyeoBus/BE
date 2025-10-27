package com.moyeobus.domain.user

data class TransportOperator (
    var id: Long? = null,
    var loginId: String,
    var password: String,
    var operatorName: String
)