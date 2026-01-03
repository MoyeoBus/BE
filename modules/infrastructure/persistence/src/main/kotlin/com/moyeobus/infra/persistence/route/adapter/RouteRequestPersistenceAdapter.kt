package com.moyeobus.infra.persistence.route.adapter

import com.moyeobus.application.localgov.port.`in`.LocalGovDateUseWrapper
import com.moyeobus.application.localgov.port.`in`.LocalGovStationWrapper
import com.moyeobus.application.localgov.port.`in`.LocalGovTimeUseWrapper
import com.moyeobus.application.route.model.RequestAddressCount
import com.moyeobus.application.route.port.out.RouteRequestOutPort
import com.moyeobus.application.route.port.out.RouteRequestPage
import com.moyeobus.application.route.port.out.RouteRequestQuery
import com.moyeobus.application.route.port.out.RouteRequestSummaryFilter
import com.moyeobus.application.route.port.out.RouteRequestSummaryProjection
import com.moyeobus.domain.route.Address
import com.moyeobus.domain.route.RequestStatus
import com.moyeobus.domain.route.RouteRequest
import com.moyeobus.global.exception.NotFoundException
import com.moyeobus.infra.persistence.address.mapper.AddressMapper
import com.moyeobus.infra.persistence.route.entity.RouteRequestEntity
import com.moyeobus.infra.persistence.route.repository.RouteRequestJpaRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneOffset
import kotlin.collections.map

@Component
class RouteRequestPersistenceAdapter(
    private val addressMapper: AddressMapper,
    private val repo: RouteRequestJpaRepository
) : RouteRequestOutPort {
    override fun save(request: RouteRequest) {
        repo.save(request.toEntity())
    }

    override fun saveAll(request: List<RouteRequest>) {
        request.map { repo.save(it.toEntity()) }
    }

    override fun countMonthly(requestIds: List<Long>, ym: YearMonth): List<LocalGovDateUseWrapper> {
        val stdDate = ym.atDay(1)

        val res = repo.countMonthlyUse(requestIds, stdDate)

        val countMap = res.associateBy(
            keySelector = { it.date },
            valueTransform = { it.useCount }
        )

        val daysInMonth = ym.lengthOfMonth()

        return (1..daysInMonth).map { day ->
            val date = ym.atDay(day)

            LocalGovDateUseWrapper(
                date = date,
                useCount = countMap[date] ?: 0
            )
        }
    }

    override fun countHourly(requestIds: List<Long>, date: LocalDate): List<LocalGovTimeUseWrapper> {
        val res = repo.countHourlyUse(requestIds, date)
        return (5..23).map { hour ->
            val found = res.find { it.hour == hour }
            if (found != null)
                LocalGovTimeUseWrapper(hour = found.hour, useCount = found.useCount)
            else
                LocalGovTimeUseWrapper(hour = hour, useCount = 0)
        }
    }

    override fun countDepartureByRouteId(routeId: Long): List<LocalGovStationWrapper> {
        val res = repo.countDepartureByRouteId(routeId)
        return res.map { LocalGovStationWrapper(it.station.name, it.count) }
    }

    override fun countDestinationByRouteId(routeId: Long): List<LocalGovStationWrapper> {
        val res = repo.countDestinationByRouteId(routeId)
        return res.map { LocalGovStationWrapper(it.station.name, it.count) }
    }

    override fun findBy(query: RouteRequestQuery): RouteRequestPage {
        val pageSize = query.limit
        val list = repo.pageBy(
            passengerId = query.passengerId,
            status = query.status?.name,
            fromAt = query.from?.toInstant(ZoneOffset.UTC),
            toAt = query.to?.toInstant(ZoneOffset.UTC),
            cursorCreatedAt = query.cursorCreatedAt?.toInstant(ZoneOffset.UTC),
            cursorId = query.cursorId,
            org = PageRequest.of(0, pageSize + 1),
        )

        val hasNext = list.size > pageSize
        val items = list.take(pageSize)
        val last = items.lastOrNull()

        return RouteRequestPage(
            items = items.map { it.toDomain() },
            hasNext = hasNext,
            nextCursorCreatedAt = last?.createdAt?.let { java.time.LocalDateTime.ofInstant(it, ZoneOffset.UTC) },
            nextCursorId = last?.id,
        )
    }

    override fun findByAddress(addresses: List<Address>): List<RouteRequest> {
        val addressIds = addresses.map { it.id!! }
        val res = repo.findByAddressIds(addressIds)
        return res.map { it.toDomain() }
    }


    override fun findById(requestId: Long): RouteRequest {
        val res = repo.findById(requestId)
            .orElseThrow { NotFoundException("RouteRequest(id=$requestId)") }
        return res.toDomain()
    }

    override fun findDeparturesByPassengerAndRoute(passengerId: Long, routeId: Long): List<String> {
        return repo.findDeparturesByPassengerAndRoute(passengerId, routeId)
    }

    override fun findByPending(): List<RouteRequest> {
        val res = repo.findByStatus(RequestStatus.PENDING.toString())
        return res.map { it.toDomain() }
    }

    override fun findByRoutes(routeIds: List<Long?>): List<RouteRequest> {
        val res = repo.findByRouteIds(routeIds)
        return res.map { it.toDomain() }
    }

    override fun findDepartureCountByRoute(routeIds: List<Long?>): List<RequestAddressCount> {
        val res = repo.findDepartureCountByRoute(routeIds)
        return res.map { RequestAddressCount(addressMapper.toDomain(it.address), it.requestCount) }
    }

    override fun findDestinationCountByRoute(routeIds: List<Long?>): List<RequestAddressCount> {
        val res = repo.findDestinationCountByRoute(routeIds)
        return res.map { RequestAddressCount(addressMapper.toDomain(it.address), it.requestCount) }
    }

    override fun summary(filter: RouteRequestSummaryFilter): RouteRequestSummaryProjection {
        val list = repo.summary(
            passengerId = 1L,
            status = filter.status?.name,
            fromAt = filter.from?.toInstant(ZoneOffset.UTC),
            toAt = filter.to?.toInstant(ZoneOffset.UTC),
        )
        val arr = list.first()
        val totalCount = (arr[0] as Number).toLong()
        val approvedCount = (arr[1] as Number).toLong()
        val cancelledCount = (arr[2] as Number).toLong()
        val pendingCount = (arr[3] as Number).toLong()
        return RouteRequestSummaryProjection(totalCount, approvedCount
            , cancelledCount, pendingCount)
    }

    private fun RouteRequest.toEntity() =
        RouteRequestEntity(
            id = this.id,
            passengerId = this.passengerId,
            routeId = this.routeId,
            departure = addressMapper.toEntity(this.departure),
            destination = addressMapper.toEntity(this.destination),
            startDateTime = this.startDateTime.toInstant(ZoneOffset.UTC),
            endDateTime = this.endDateTime.toInstant(ZoneOffset.UTC),
            status = this.status.name
        )
    private fun RouteRequestEntity.toDomain() =
        RouteRequest(
            id = this.id,
            passengerId = this.passengerId,
            routeId = this.routeId,
            departure = addressMapper.toDomain(this.departure),
            destination = addressMapper.toDomain(this.destination),
            startDateTime = LocalDateTime.ofInstant(this.startDateTime, ZoneOffset.UTC),
            endDateTime = LocalDateTime.ofInstant(this.endDateTime, ZoneOffset.UTC),
            status = RequestStatus.valueOf(this.status)
        )
}