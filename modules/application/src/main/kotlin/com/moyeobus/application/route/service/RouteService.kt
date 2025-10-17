package com.moyeobus.application.route.service

import com.moyeobus.application.route.port.`in`.RouteCommand
import com.moyeobus.application.route.port.`in`.RouteRequestResult
import com.moyeobus.application.route.port.`in`.RouteUseCase
import com.moyeobus.application.route.port.out.RouteRequestOutPort
import com.moyeobus.domain.route.RequestStatus
import com.moyeobus.domain.route.RouteRequest
import org.springframework.stereotype.Service

@Service
class RouteService(
    private val routeRequestRepository: RouteRequestOutPort
) : RouteUseCase {
    override fun request(command: RouteCommand) {
        val routeRequest = RouteRequest(
            passengerId = 1L,
            departureId = command.departureId,
            destinationId = command.destinationId,
            startDateTime = command.startDateTime,
            endDateTime = command.endDateTime,
            status = RequestStatus.PENDING
        )
        routeRequestRepository.save(routeRequest)
    }

    // TODO: 페이지네이션 추가
    override fun query(): RouteRequestResult {
        val res = routeRequestRepository.findBy(1L)
        return res
    }

    override fun cancel(requestId: Long) {
        val res = routeRequestRepository.findById(requestId)
        val cancelled = res.cancel()
        routeRequestRepository.save(cancelled)
    }
}