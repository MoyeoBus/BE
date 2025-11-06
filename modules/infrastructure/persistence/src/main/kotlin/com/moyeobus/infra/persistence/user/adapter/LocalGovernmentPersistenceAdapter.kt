package com.moyeobus.infra.persistence.user.adapter

import com.moyeobus.application.user.port.out.LocalGovernmentOutPort
import com.moyeobus.domain.user.LocalGovernment
import com.moyeobus.infra.exception.NotFoundException
import com.moyeobus.infra.persistence.address.mapper.LocalGovernmentMapper
import com.moyeobus.infra.persistence.user.entity.LocalGovernmentEntity
import com.moyeobus.infra.persistence.user.repository.LocalGovernmentJpaRepository
import org.springframework.stereotype.Component

@Component
class LocalGovernmentPersistenceAdapter(
    private val mapper: LocalGovernmentMapper,
    private val repo: LocalGovernmentJpaRepository
) : LocalGovernmentOutPort {
    override fun findById(id: Long): LocalGovernment {
        val res = repo.findById(id).
                orElseThrow { NotFoundException("LocalGovernment(id=$id)") }

        return mapper.toDomain(res)
    }

    override fun checkExists(id: Long): Boolean {
        return repo.existsById(id)
    }

}