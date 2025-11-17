package com.moyeobus.api.localgov.dto

import com.moyeobus.application.survey.model.RequestSurveySummary

data class SurveyPieResult(
    val items: List<RequestSurveySummary>
)