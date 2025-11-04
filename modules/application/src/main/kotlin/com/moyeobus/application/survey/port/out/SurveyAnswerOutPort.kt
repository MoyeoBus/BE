package com.moyeobus.application.survey.port.out

import com.moyeobus.domain.survey.SurveyAnswer

interface SurveyAnswerOutPort {
    fun save(answer: SurveyAnswer)
}