package com.moyeobus.application.survey.port.out

import com.moyeobus.domain.survey.SurveyOption

interface SurveyOptionOutPort {
    fun checkExists(id: Long) : Boolean
    fun findAll() : List<SurveyOption>
    fun findBy(id: Long) : SurveyOption
    fun save(reason: SurveyOption)
}