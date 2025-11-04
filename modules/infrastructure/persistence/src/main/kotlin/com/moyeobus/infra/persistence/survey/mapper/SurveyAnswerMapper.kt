package com.moyeobus.infra.persistence.survey.mapper

import com.moyeobus.domain.survey.SurveyAnswer
import com.moyeobus.infra.persistence.survey.entity.SurveyAnswerEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface SurveyAnswerMapper {
    fun toDomain(entity: SurveyAnswerEntity) : SurveyAnswer
    fun toEntity(domain: SurveyAnswer) : SurveyAnswerEntity
}