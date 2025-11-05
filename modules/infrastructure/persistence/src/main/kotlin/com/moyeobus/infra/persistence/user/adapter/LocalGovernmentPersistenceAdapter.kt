package com.moyeobus.infra.persistence.user.adapter

import com.moyeobus.application.user.port.out.LocalGovernmentOutPort
import com.moyeobus.domain.user.LocalGovernment
import com.moyeobus.infra.exception.NotFoundException
import com.moyeobus.infra.persistence.user.entity.LocalGovernmentEntity
import com.moyeobus.infra.persistence.user.repository.LocalGovernmentJpaRepository
import org.springframework.stereotype.Component

@Component
// TODO : Mapper로 변환
class LocalGovernmentPersistenceAdapter(
    private val repo: LocalGovernmentJpaRepository
) : LocalGovernmentOutPort {
    override fun findById(id: Long): LocalGovernment {
        val res = repo.findById(id).
                orElseThrow { NotFoundException("LocalGovernment(id=$id)") }

        return res.toDomain()
    }

    override fun checkExists(id: Long): Boolean {
        return repo.existsById(id)
    }

    private fun LocalGovernmentEntity.toDomain() =
        LocalGovernment(
            id = this.id,
            loginId = this.loginId,
            govName = this.govName
        )
}