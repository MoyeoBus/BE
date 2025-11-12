package com.moyeobus.application.localgov.port.service

import com.moyeobus.application.address.port.out.AddressOutPort
import com.moyeobus.application.address.port.out.AreaOutPort
import com.moyeobus.application.localgov.port.out.LocalGovernmentOutPort
import com.moyeobus.application.localgov.port.out.LocalGovernmentUseCase
import com.moyeobus.application.route.port.out.RouteRequestOutPort
import org.springframework.stereotype.Service

@Service
class LocalGovernmentQueryService(
    private val areaRepository: AreaOutPort,
    private val addressRepository: AddressOutPort,
    private val routeRequestRepository: RouteRequestOutPort,
    private val localGovRepository: LocalGovernmentOutPort
) : LocalGovernmentUseCase {
    override fun queryLocal(id: Long): Any {
        val localGov = localGovRepository.findById(id)
        val govArea = localGov.area

        val sigunguList = govArea.id?.let { areaRepository.findChildrenByParent(it) }
        val addressList = sigunguList?.let { addressRepository.findAllByArea(it) }

        val requestList = addressList?.let { routeRequestRepository.findByAddress(it) }
        val areaCounts = requestList
            ?.map { it.destination.area }   // area 자체 추출
            ?.groupingBy { it }                    // AreaEntity 단위로 그룹핑
            ?.eachCount()                          // 각 AreaEntity의 등장 횟수
            ?: emptyMap()

        val total = areaCounts.values.sum().toDouble()

        val areaStats = areaCounts.map { (key, count) ->
            mapOf(
                "areaId" to key.id,
                "sigunguName" to key.sigunguName,
                "count" to count,
                "ratio" to String.format("%.2f", count / total * 100)
            )
        }


        return areaStats
    }
}