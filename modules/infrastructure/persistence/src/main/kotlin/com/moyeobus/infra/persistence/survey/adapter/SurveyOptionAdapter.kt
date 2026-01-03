package com.moyeobus.infra.persistence.survey.adapter

import com.moyeobus.application.survey.port.out.SurveyOptionOutPort
import com.moyeobus.domain.survey.SurveyOption
import com.moyeobus.global.exception.NotFoundException
import com.moyeobus.infra.persistence.survey.repository.SurveyOptionJpaRepository
import com.moyeobus.infra.persistence.survey.mapper.SurveyOptionMapper
import org.springframework.stereotype.Component

@Component
class SurveyOptionAdapter(
    private val mapper: SurveyOptionMapper,
    private val repo: SurveyOptionJpaRepository
) : SurveyOptionOutPort {
    override fun checkExists(id: Long): Boolean {
        return  repo.existsById(id)
    }

    override fun findAll(): List<SurveyOption> {
        val res = repo.findAll()
        return res.map({ mapper.toDomain(it) })
    }

    override fun findBy(id: Long) : SurveyOption {
        val reason = repo.findById(id)
            .orElseThrow { NotFoundException("SurveyOption(id=$id)") }
        return mapper.toDomain(reason)
    }

    override fun save(reason: SurveyOption) {
        repo.save(mapper.toEntity(reason))
    }
}