package com.moyeobus.api.operator.dto

import com.moyeobus.application.survey.model.RequestSurveySummary

data class RequestSurveyResult(
    val items: List<RequestSurveySummary>
)
