package com.moyeobus.infra.persistence.address.adapter

import com.moyeobus.application.address.port.out.AreaOutPort
import com.moyeobus.domain.route.Area
import com.moyeobus.infra.exception.NotFoundException
import com.moyeobus.infra.persistence.address.mapper.AreaMapper
import com.moyeobus.infra.persistence.address.repotiory.AreaJpaRepository
import org.springframework.stereotype.Component

@Component
class AreaPersistenceAdapter(
    private val mapper: AreaMapper,
    private val repo: AreaJpaRepository
) : AreaOutPort {
    override fun findById(id: Long) : Area{
        val res = repo.findById(id).orElseThrow(
            { NotFoundException("Area(id=$id)") })
        return mapper.toDomain(res)
    }

    override fun findDosiId(dosi: String): Long {
        return repo.findDosiId(dosi)
    }

    override fun findSigunguByDosi(dosiId: Long, sigungu: String) : Area {
        val res = repo.findSigunguByDosi(dosiId, sigungu)
        return mapper.toDomain(res)
    }

    override fun findChildrenByParent(id: Long): List<Area> {
        val res = repo.findAllChildren(id)
        return res.map { mapper.toDomain(it) }
    }
}