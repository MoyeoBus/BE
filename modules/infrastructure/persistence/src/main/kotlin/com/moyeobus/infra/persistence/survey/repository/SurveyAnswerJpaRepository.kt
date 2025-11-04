package com.moyeobus.infra.persistence.survey.repository

import com.moyeobus.infra.persistence.survey.entity.SurveyAnswerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SurveyAnswerJpaRepository : JpaRepository<SurveyAnswerEntity, Long> {
}