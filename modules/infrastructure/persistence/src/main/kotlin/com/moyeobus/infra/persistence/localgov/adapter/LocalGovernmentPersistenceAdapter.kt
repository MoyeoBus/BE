package com.moyeobus.infra.persistence.localgov.adapter

import com.moyeobus.application.localgov.port.out.LocalGovernmentOutPort
import com.moyeobus.domain.user.LocalGovernment
import com.moyeobus.global.exception.NotFoundException
import com.moyeobus.infra.persistence.address.mapper.LocalGovernmentMapper
import com.moyeobus.infra.persistence.localgov.repository.LocalGovernmentJpaRepository
import org.springframework.stereotype.Component

@Component
class LocalGovernmentPersistenceAdapter(
    private val mapper: LocalGovernmentMapper,
    private val repo: LocalGovernmentJpaRepository
) : LocalGovernmentOutPort {
    override fun findById(id: Long): LocalGovernment {
        val res = repo.findById(id).orElseThrow({ NotFoundException("LocalGovernment(id=$id)") })
        return mapper.toDomain(res)
    }
}