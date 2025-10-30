package com.moyeobus.domain.bus

data class Bus (
    val id: Long? = null,

    val operatorId: Long,

    val busNumber: Int,

    val carNumber: String,

    var status: BusStatus
) {
    fun switch(status: BusStatus) {
        this.status = status
    }
}

enum class BusStatus { OPERATING, IDLE }


/**
 *  운수사 대시보드에서 버스 현황 파악에 이용.
 */
//data class BusSummary (
//    val totalCount: Long,
//    val operatingCount: Long,
//    val waitingCount: Long
//)