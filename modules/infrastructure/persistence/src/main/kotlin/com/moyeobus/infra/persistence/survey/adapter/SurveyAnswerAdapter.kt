package com.moyeobus.infra.persistence.survey.adapter

import com.moyeobus.application.survey.model.RequestSurveySummary
import com.moyeobus.application.survey.port.out.SurveyAnswerOutPort
import com.moyeobus.domain.survey.SurveyAnswer
import com.moyeobus.infra.persistence.survey.mapper.SurveyAnswerMapper
import com.moyeobus.infra.persistence.survey.repository.SurveyAnswerJpaRepository
import org.springframework.stereotype.Component

@Component
class SurveyAnswerAdapter(
    private val mapper: SurveyAnswerMapper,
    private val repo: SurveyAnswerJpaRepository
) : SurveyAnswerOutPort{
    override fun save(answer: SurveyAnswer) {
        repo.save(mapper.toEntity(answer))
    }

    override fun queryByLocal(localGovId: Long): List<RequestSurveySummary> {
        val res = repo.countByLocalGovId(localGovId)
        return res.map { RequestSurveySummary(it.reason, it.count, it.ratio) }
    }

}