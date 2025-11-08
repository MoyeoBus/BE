package com.moyeobus.application.route.service

import com.moyeobus.application.address.port.out.AddressOutPort
import com.moyeobus.application.route.port.`in`.RouteCommand
import com.moyeobus.application.route.port.`in`.RouteRequestUseCase
import com.moyeobus.application.route.port.out.RouteRequestOutPort
import com.moyeobus.domain.route.RequestStatus
import com.moyeobus.domain.route.RouteRequest
import org.springframework.stereotype.Service

@Service
class RouteRequestService(
    private val addressRepository: AddressOutPort,
    private val routeRequestRepository: RouteRequestOutPort
) : RouteRequestUseCase {
    override fun request(command: RouteCommand) {
        val departure = addressRepository.findById(command.departureId)
        val destination = addressRepository.findById(command.destinationId)
        val routeRequest = RouteRequest(
            passengerId = 1L,
            departure = departure,
            destination = destination,
            startDateTime = command.startDateTime,
            endDateTime = command.endDateTime,
            status = RequestStatus.PENDING
        )
        routeRequestRepository.save(routeRequest)
    }

    override fun cancel(requestId: Long) {
        val res = routeRequestRepository.findById(requestId)
        val cancelled = res.cancel()
        routeRequestRepository.save(cancelled)
    }
}