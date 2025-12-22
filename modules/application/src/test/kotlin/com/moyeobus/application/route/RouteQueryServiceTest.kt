package com.moyeobus.application.route

import com.moyeobus.application.address.port.out.AreaOutPort
import com.moyeobus.application.bus.port.out.BusOutPort
import com.moyeobus.application.route.model.RouteItem
import com.moyeobus.application.route.port.`in`.LocalQueryFilter
import com.moyeobus.application.route.port.out.LocalRoutePage
import com.moyeobus.application.route.port.out.LocalRouteQuery
import com.moyeobus.application.route.port.out.RouteComponentOutPort
import com.moyeobus.application.route.port.out.RouteOutPort
import com.moyeobus.application.route.port.out.RouteTimeRange
import com.moyeobus.application.route.service.RouteQueryService
import com.moyeobus.domain.route.Area
import com.moyeobus.domain.route.GeoPoint
import com.moyeobus.domain.route.Route
import com.moyeobus.domain.route.RouteComponent
import com.moyeobus.domain.route.RouteStatus
import com.moyeobus.global.util.CursorUtil
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
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

    @Test
    fun `정류장이 하나만 있는 경우 출발지와 도착지가 동일하다`() {
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
        val singleStation = listOf(
            RouteItem(order = 1, station = "석계동", time = "20:40")
        )

        every { routeRepo.findById(routeId) } returns expectedRoute
        every { busRepo.findNumberById(3L) } returns 3000L
        every { routeComponentRepo.findTimeRange(routeId) } returns RouteTimeRange(
            date = "2025-12-21",
            departureTime = "20:37",
            destinationTime = "20:55"
        )
        every { routeComponentRepo.findAllByRouteId(routeId) } returns singleStation

        // when
        val result = service.queryRouteDetail(routeId)

        // then
        assertEquals("석계동", result.routeInfo.departureName)
        assertEquals("석계동", result.routeInfo.destinationName)
    }

    @Test
    fun `정류장이 없는 경우 출발지와 도착지가 UNKNOWN이다`() {
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

        every { routeRepo.findById(routeId) } returns expectedRoute
        every { busRepo.findNumberById(3L) } returns 3000L
        every { routeComponentRepo.findTimeRange(routeId) } returns RouteTimeRange(
            date = "2025-12-21",
            departureTime = "20:37",
            destinationTime = "20:55"
        )
        every { routeComponentRepo.findAllByRouteId(routeId) } returns emptyList()

        // when
        val result = service.queryRouteDetail(routeId)

        // then
        assertEquals("UNKNOWN", result.routeInfo.departureName)
        assertEquals("UNKNOWN", result.routeInfo.destinationName)
    }

    @Test
    fun `지역별로 노선을 조회한다`() {
        // given
        val filter = createLocalQueryFilter()
        val dosi = filter.dosi
        val sigungu = filter.sigungu
        val decodedCursor = CursorUtil.decode(filter.cursor)

        every { areaRepo.findDosiId(dosi) } returns Result.success(11000L)
        val dosiId = areaRepo.findDosiId(dosi).getOrThrow()

        every { areaRepo.findSigunguByDosi(dosiId, sigungu) } returns Result.success(createArea())
        val sigunguArea = areaRepo.findSigunguByDosi(dosiId, sigungu).getOrThrow()

        val localRouteQuery = LocalRouteQuery.from(sigunguArea.id!!,filter, decodedCursor)
        every { routeRepo.findBy(localRouteQuery) } returns createLocalRoutePage()
        every { routeComponentRepo.findAllByRouteIdIn(listOf(1L)) } returns listOf(createRouteComponent())

        // when
        val queryItems = service.queryLocalRoute(filter)

        // then
        assertEquals(1,queryItems.items.size)
    }

    private fun createArea(
        id: Long? = 11000L,
        sigunguName: String = "종로구",
        parentSigunguId: Long? = 10000L
    ) = Area(id, sigunguName, parentSigunguId)

    private fun createRouteComponent(
        id: Long? = null,
        routeId: Long = 1L,
        name: String = "석계역",
        location: GeoPoint = GeoPoint(37.5665, 126.9780),
        assignedTime: LocalDateTime = LocalDateTime.of(2025, 12, 21, 20, 40),
        distance: Int = 100,
        duration: Int = 5,
        isRequested: Boolean = false
    ) = RouteComponent(
        id = id,
        routeId = routeId,
        name = name,
        location = location,
        assignedTime = assignedTime,
        distance = distance,
        duration = duration,
        isRequested = isRequested
    )

    private fun createLocalQueryFilter(
        dosi: String = "서울특별시",
        sigungu: String = "종로구",
        status: String? = null,
        from: LocalDateTime? = null,
        to: LocalDateTime? = null,
        cursor: String? = null,
        limit: Int = 20
    ) = LocalQueryFilter(dosi, sigungu, status, from, to, cursor, limit)

    private fun createLocalRoutePage(
        items: List<Route> = listOf(
            Route(
                id = 1L,
                operatorId = 1L,
                localGovId = 1L,
                busId = 3L,
                routeDistance = 100,
                routeTotalTime = 30,
                status = RouteStatus.OPERATED
            )
        ),
        hasNext: Boolean = false,
        nextCursorCreatedAt: LocalDateTime? = null,
        nextCursorId: Long? = null,
    ) = LocalRoutePage(items, hasNext, nextCursorCreatedAt, nextCursorId)

}
