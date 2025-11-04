package com.moyeobus.infra.persistence.survey.repository

import com.moyeobus.infra.persistence.survey.entity.SurveyOptionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SurveyOptionJpaRepository : JpaRepository<SurveyOptionEntity, Long> {
}