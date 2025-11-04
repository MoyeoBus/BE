package com.moyeobus.application.survey.service

import com.moyeobus.application.survey.port.`in`.SurveyAnswerUseCase
import com.moyeobus.application.survey.port.`in`.SurveyCommand
import com.moyeobus.application.survey.port.`in`.SurveyOptionResult
import com.moyeobus.application.survey.port.`in`.SurveyOptionUseCase
import com.moyeobus.application.survey.port.out.SurveyAnswerOutPort
import com.moyeobus.application.survey.port.out.SurveyOptionOutPort
import com.moyeobus.application.user.port.out.LocalGovernmentOutPort
import com.moyeobus.domain.survey.SurveyAnswer
import org.springframework.stereotype.Service

@Service
class SurveyService(
    private val localGovRepository: LocalGovernmentOutPort,
    private val surveyOptionRepository: SurveyOptionOutPort,
    private val surveyAnswerRepository: SurveyAnswerOutPort
) : SurveyAnswerUseCase, SurveyOptionUseCase {

    override fun create(command: SurveyCommand) {
        val optionId = command.optionId
        val departureId = command.departureId
        val destinationId = command.destinationId

        if(!surveyOptionRepository.checkExists(optionId)) {
            throw IllegalArgumentException("Contain invalid option id")
        }

        if (localGovRepository.checkExists(departureId) && localGovRepository.checkExists(destinationId)) {
            val answer = SurveyAnswer(null, command.departureId,
                command.departureId, command.optionId)
            surveyAnswerRepository.save(answer)
        } else {
            throw IllegalArgumentException("Contain invalid spot id")
        }

    }

    override fun queryAll(): SurveyOptionResult {
        val res = surveyOptionRepository.findAll()
        return SurveyOptionResult(res)
    }

}