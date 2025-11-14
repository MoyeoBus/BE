package com.moyeobus.infra.persistence.survey.dto

interface SurveyProjection {
    val reason: String
    val count: Int
    val ratio: String
}