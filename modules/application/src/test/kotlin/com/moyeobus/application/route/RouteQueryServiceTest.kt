package com.moyeobus.application.route

import com.moyeobus.application.address.port.out.AreaOutPort
import com.moyeobus.application.bus.port.out.BusOutPort
import com.moyeobus.application.route.model.RouteDetail
import com.moyeobus.application.route.model.RouteInfo
import com.moyeobus.application.route.model.RouteItem
import com.moyeobus.application.route.port.out.RouteComponentOutPort
import com.moyeobus.application.route.port.out.RouteOutPort
import com.moyeobus.application.route.port.out.RouteTimeRange
import com.moyeobus.application.route.service.RouteQueryService
import com.moyeobus.domain.route.Route
import com.moyeobus.domain.route.RouteStatus
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals


class RouteQueryServiceTest {
    private val areaRepo = mockk<AreaOutPort>()
    private val busRepo = mockk<BusOutPort>()
    private val routeRepo = mockk<RouteOutPort>()
    private val routeComponentRepo = mockk<RouteComponentOutPort>()

    private val service = RouteQueryService(areaRepo, busRepo, routeRepo, routeComponentRepo)


    @Test
    fun `노선 세부정보를 조회한다`() {
        // given
        val routeId = 1L
        val expectedRoute = Route(
            id = routeId,
            operatorId = 1L,
            localGovId = 1L,
            busId = 3L,
            routeDistance = 100,
            routeTotalTime = 30,
            status = RouteStatus.OPERATED
        )
        val expectedStations = listOf(
            RouteItem(order = 1, station = "석계동", time = "20:40"),
            RouteItem(order = 2, station = "석계2동", time = "20:45"),
            RouteItem(order = 3, station = "석계3동", time = "20:50")
        )

        every { routeRepo.findById(routeId) } returns expectedRoute
        every { busRepo.findNumberById(3L) } returns 3000L
        every { routeComponentRepo.findTimeRange(routeId) } returns RouteTimeRange(
            date = "2025-12-21",
            departureTime = "20:37",
            destinationTime = "20:55"
        )
        every { routeComponentRepo.findAllByRouteId(routeId) } returns expectedStations

        // when
        val result = service.queryRouteDetail(routeId)

        // then
        assertEquals(3000L, result.routeInfo.busNumber)
        assertEquals("석계동", result.routeInfo.departureName)
        assertEquals("석계3동", result.routeInfo.destinationName)
        assertEquals("2025-12-21", result.routeInfo.operateDate)
        assertEquals("20:37", result.routeInfo.departTime)
        assertEquals("20:55", result.routeInfo.arrivalTime)
        assertEquals(3, result.items.size)
        assertEquals(expectedStations, result.items)
    }
}