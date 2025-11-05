package com.moyeobus.application.survey.service

import com.moyeobus.application.address.port.out.AddressOutPort
import com.moyeobus.application.exception.InvalidOptionException
import com.moyeobus.application.exception.InvalidSpotException
import com.moyeobus.application.survey.port.`in`.SurveyAnswerUseCase
import com.moyeobus.application.survey.port.`in`.SurveyCommand
import com.moyeobus.application.survey.port.`in`.SurveyOptionResult
import com.moyeobus.application.survey.port.`in`.SurveyOptionUseCase
import com.moyeobus.application.survey.port.out.SurveyAnswerOutPort
import com.moyeobus.application.survey.port.out.SurveyOptionOutPort
import com.moyeobus.domain.survey.SurveyAnswer
import org.springframework.stereotype.Service

@Service
class SurveyService(
    private val addressRepository: AddressOutPort,
    private val surveyOptionRepository: SurveyOptionOutPort,
    private val surveyAnswerRepository: SurveyAnswerOutPort
) : SurveyAnswerUseCase, SurveyOptionUseCase {

    override fun create(command: SurveyCommand) {
        val optionId = command.optionId
        val departureId = command.departureId
        val destinationId = command.destinationId


        if (!surveyOptionRepository.checkExists(optionId)) {
            throw InvalidOptionException(optionId)
        }

        if (!addressRepository.checkExists(departureId)) {
            throw InvalidSpotException(departureId)
        }

        if (!addressRepository.checkExists(destinationId)) {
            throw InvalidSpotException(destinationId)
        }

        val answer = SurveyAnswer(null, command.departureId,
            command.destinationId, command.optionId)

        surveyAnswerRepository.save(answer)
    }

    override fun queryAll(): SurveyOptionResult {
        val res = surveyOptionRepository.findAll()
        return SurveyOptionResult(res)
    }

}