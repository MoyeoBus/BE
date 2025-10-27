package com.moyeobus.infra.persistence.user.adapter

import com.moyeobus.application.user.port.out.TransportOperatorOutPort
import com.moyeobus.domain.user.TransportOperator
import com.moyeobus.infra.persistence.user.repository.TransportOperatorJpaRepository
import org.springframework.stereotype.Component

@Component
class TransportOperatorPersistenceAdapter(
    private val repo: TransportOperatorJpaRepository
) : TransportOperatorOutPort {
    override fun findById(id: Long): TransportOperator {
        TODO("Not yet implemented")
    }
}