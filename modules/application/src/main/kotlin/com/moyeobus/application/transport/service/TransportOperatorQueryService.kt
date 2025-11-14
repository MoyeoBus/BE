package com.moyeobus.application.transport.service

import com.moyeobus.application.route.model.RequestAddressCount
import com.moyeobus.application.route.model.RequestAreaRanking
import com.moyeobus.application.route.model.RequestStationRanking
import com.moyeobus.application.route.model.RouteDistanceRanking
import com.moyeobus.application.route.port.out.RouteOutPort
import com.moyeobus.application.route.port.out.RouteRequestOutPort
import com.moyeobus.application.transport.port.`in`.TransportOperatorQueryUseCase
import org.springframework.stereotype.Service

@Service
class TransportOperatorQueryService(
    private val routeRepository: RouteOutPort,
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
                    distance = route.routeDistance,
                    ranking = index + 1
                )
            }
    }
}