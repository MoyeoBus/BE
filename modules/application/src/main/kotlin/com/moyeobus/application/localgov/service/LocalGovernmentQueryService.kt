package com.moyeobus.application.localgov.service

import com.moyeobus.application.address.port.out.AddressOutPort
import com.moyeobus.application.address.port.out.AreaOutPort
import com.moyeobus.application.localgov.port.`in`.LocalGovDateResult
import com.moyeobus.application.localgov.port.`in`.LocalGovRouteResult
import com.moyeobus.application.localgov.port.`in`.LocalGovRouteWrapper
import com.moyeobus.application.localgov.port.`in`.LocalGovStatusResult
import com.moyeobus.application.localgov.port.`in`.LocalGovStatusWrapper
import com.moyeobus.application.localgov.port.`in`.LocalGovTimeResult
import com.moyeobus.application.localgov.port.out.LocalGovernmentOutPort
import com.moyeobus.application.localgov.port.`in`.LocalGovernmentUseCase
import com.moyeobus.application.route.port.out.RouteComponentOutPort
import com.moyeobus.application.route.port.out.RouteOutPort
import com.moyeobus.application.route.port.out.RouteRequestOutPort
import com.moyeobus.application.routeowner.port.out.PassengerRouteOutPort
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.YearMonth

@Service
class LocalGovernmentQueryService(
    private val areaRepository: AreaOutPort,
    private val addressRepository: AddressOutPort,
    private val routeRepository: RouteOutPort,
    private val routeRequestRepository: RouteRequestOutPort,
    private val routeComponentRepository: RouteComponentOutPort,
    private val localGovRepository: LocalGovernmentOutPort,
    private val passengerRouteRepository: PassengerRouteOutPort
) : LocalGovernmentUseCase {
    override fun queryLocal(id: Long): LocalGovStatusResult {
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
            LocalGovStatusWrapper(
                areaId = key.id!!,
                sigunguName = key.sigunguName,
                count = count,
                ratio = String.format("%.2f", count / total * 100)

            )
        }


        return LocalGovStatusResult(govArea.sigunguName, areaStats)
    }

    override fun queryDate(id: Long, stdDate: YearMonth): LocalGovDateResult {
        val localGov = localGovRepository.findById(id)
        val govArea = localGov.area

        val sigunguList = govArea.id?.let { areaRepository.findChildrenByParent(it) }
        val addressList = sigunguList?.let { addressRepository.findAllByArea(it) }

        val requestList = addressList?.let { routeRequestRepository.findByAddress(it) }

        val requestListIds = requestList
            ?.mapNotNull { it.id }
            ?: emptyList()

        val localGovDateResult = routeRequestRepository.countMonthly(requestListIds, stdDate)
        return LocalGovDateResult(govArea.sigunguName, localGovDateResult)
    }

    override fun queryHour(
        id: Long,
        date: LocalDate
    ): LocalGovTimeResult {
        val localGov = localGovRepository.findById(id)
        val govArea = localGov.area

        val sigunguList = govArea.id?.let { areaRepository.findChildrenByParent(it) }
        val addressList = sigunguList?.let { addressRepository.findAllByArea(it) }

        val requestList = addressList?.let { routeRequestRepository.findByAddress(it) }

        val requestListIds = requestList
            ?.mapNotNull { it.id }
            ?: emptyList()

        val localGovTimeResult = routeRequestRepository.countHourly(requestListIds, date)
        return LocalGovTimeResult(govArea.sigunguName, localGovTimeResult)
    }

    override fun queryRoute(id: Long): LocalGovRouteResult {
        val localGov = localGovRepository.findById(id)
        val govName = localGov.area.sigunguName

        val routes = routeRepository.findByLocal(localGov.area.id!!)

        val items = routes.map { route ->
            val routeId = route.id!!

            val stationCount = routeComponentRepository.countStations(routeId)
            val peopleCount = passengerRouteRepository.countByLocal(routeId)

            LocalGovRouteWrapper(
                routeId = routeId,
                stationCount = stationCount,
                peopleCount = peopleCount,
                distance = (route.routeDistance / 1000.0).let { "%.1f".format(it).toDouble() }
            )
        }

        return LocalGovRouteResult(
            govName = govName,
            items = items
        )
    }
}