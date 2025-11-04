package com.moyeobus.infra.persistence.survey.mapper

import com.moyeobus.domain.survey.SurveyOption
import com.moyeobus.infra.persistence.survey.entity.SurveyOptionEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface SurveyOptionMapper {

    fun toDomain(entity: SurveyOptionEntity) : SurveyOption

    fun toEntity(domain: SurveyOption) : SurveyOptionEntity
}