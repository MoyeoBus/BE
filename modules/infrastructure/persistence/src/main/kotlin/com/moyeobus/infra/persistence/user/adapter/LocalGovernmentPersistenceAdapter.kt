package com.moyeobus.infra.persistence.user.adapter

import com.moyeobus.application.user.port.out.LocalGovernmentOutPort
import com.moyeobus.domain.user.LocalGovernment
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
                orElseThrow { IllegalArgumentException("LocalGovernment not found: $id")  }

        return res.toDomain()
    }

    private fun LocalGovernmentEntity.toDomain() =
        LocalGovernment(
            id = this.id,
            loginId = this.loginId,
            govName = this.govName
        )
}