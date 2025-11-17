package com.moyeobus.application.transport.service

import com.moyeobus.application.localgov.port.`in`.OperationHistory
import com.moyeobus.application.route.model.BusUsageCount
import com.moyeobus.application.route.model.RequestAddressCount
import com.moyeobus.application.route.model.RequestAreaRanking
import com.moyeobus.application.route.model.RequestStationRanking
import com.moyeobus.application.route.model.RouteDistanceRanking
import com.moyeobus.application.route.model.RouteTrackInfo
import com.moyeobus.application.route.model.TrackItemOutput
import com.moyeobus.application.route.port.out.RouteComponentOutPort
import com.moyeobus.application.route.port.out.RouteOutPort
import com.moyeobus.application.route.port.out.RouteRequestOutPort
import com.moyeobus.application.transport.port.`in`.TransportOperatorQueryUseCase
import com.moyeobus.domain.route.GeoPoint
import org.springframework.stereotype.Service

@Service
class TransportOperatorQueryService(
    private val routeRepository: RouteOutPort,
    private val routeComponentRepository: RouteComponentOutPort,
    private val routeRequestRepository: RouteRequestOutPort
) : TransportOperatorQueryUseCase {
    override fun queryRouteLocalTop5(id: Long): List<RequestAreaRanking> {
        val routes = routeRepository.findByOperator(id)
        val routeIds = routes.map { it.id }

        val departure = routeRequestRepository.findDepartureCountByRoute(routeIds)
        val destination = routeRequestRepository.findDestinationCountByRoute(routeIds)

        val merged = (departure + destination)
            .groupBy { it.address }
            .map { (address, list) ->
                RequestAddressCount(
                    address = address,
                    requestCount = list.sumOf { it.requestCount }
                )
            }
            .sortedByDescending { it.requestCount }
            .take(5)

        return merged.mapIndexed { index, it ->
            RequestAreaRanking(
                areaName = it.address.area.sigunguName,
                ranking = index + 1,
                requestCount = it.requestCount
            )
        }

    }

    override fun queryRouteStationTop5(id: Long): List<RequestStationRanking> {
        val routes = routeRepository.findByOperator(id)
        val routeIds = routes.map { it.id }

        val departure = routeRequestRepository.findDepartureCountByRoute(routeIds)
        val destination = routeRequestRepository.findDestinationCountByRoute(routeIds)

        val merged = (departure + destination)
            .groupBy { it.address }
            .map { (address, list) ->
                RequestAddressCount(
                    address = address,
                    requestCount = list.sumOf { it.requestCount }
                )
            }
            .sortedByDescending { it.requestCount }
            .take(5)

        return merged.mapIndexed { index, it ->
            RequestStationRanking(
                stationName = it.address.name,
                ranking = index + 1,
                requestCount = it.requestCount
            )
        }
    }

    override fun queryRouteDistanceTop5(id: Long): List<RouteDistanceRanking> {
        val routes = routeRepository.findByOperator(id)
        return routes
            .sortedByDescending { it.routeDistance }
            .take(5)
            .mapIndexed { index, route ->
                RouteDistanceRanking(
                    routeNo = route.id!!,
                    distance = (route.routeDistance / 1000.0).let { "%.1f".format(it).toDouble() },
                    ranking = index + 1
                )
            }
    }

    override fun queryBusUsage(operatorId: Long): BusUsageCount {
        val count = routeRepository.countBusUsage(operatorId)
        return count
    }

    override fun queryTodayOperate(operatorId: Long): Int {
        val routes = routeRepository.findNotCompletedByOperator(operatorId)
        val routeIds = routes.map { it.id!! }

        return routeComponentRepository.countTodayOperate(routeIds)
    }

    override fun queryHistory(operatorId: Long): List<OperationHistory> {
        val routes = routeRepository.findByOperator(operatorId)
        val routeIds = routes.map { it.id!! }

        val histories = routeIds.map { routeId ->
            val stations = routeComponentRepository.findAllByRouteId(routeId)

            val firstStationName = stations.firstOrNull()?.station ?: "UNKNOWN"
            val lastStationName = stations.lastOrNull()?.station ?: "UNKNOWN"

            val status = routeRepository.findStatus(routeId)

            OperationHistory(
                routeId = routeId,
                departureNm = firstStationName,
                destinationNm = lastStationName,
                status =status
            )
        }
        return histories
    }

    override fun queryRouteTrackInfos(routeId: Long, currentStation: String): RouteTrackInfo {
        return routeComponentRepository.findTrackInfo(routeId, currentStation)
    }

    override fun queryRouteTrackItems(routeId: Long): List<TrackItemOutput> {
        return routeComponentRepository.findTrackItems(routeId)
    }

    override fun queryRouteTrackPoints(routeId: Long): List<GeoPoint> {
        return routeComponentRepository.findTrackPoints(routeId)
    }
}