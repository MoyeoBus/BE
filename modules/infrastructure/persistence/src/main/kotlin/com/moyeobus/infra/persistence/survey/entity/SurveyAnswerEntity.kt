package com.moyeobus.infra.persistence.survey.entity

import com.moyeobus.global.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "survey_answer")
class SurveyAnswerEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val departureLocalGovId: Long,

    val destinationLocalGovId: Long,

    val surveyOptionId: Long
) : BaseEntity()
