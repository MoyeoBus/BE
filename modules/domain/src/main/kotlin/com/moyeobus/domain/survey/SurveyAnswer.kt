package com.moyeobus.domain.survey

data class SurveyAnswer(
    val id: Long? = null,

    val departureLocalGovId: Long,

    val destinationLocalGovId: Long,

    val surveyOptionId: Long
)
