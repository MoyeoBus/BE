package com.moyeobus.api.survey.controller

import com.moyeobus.api.docs.SurveyControllerDocs
import com.moyeobus.application.survey.port.`in`.SurveyAnswerUseCase
import com.moyeobus.application.survey.port.`in`.SurveyCommand
import com.moyeobus.application.survey.port.`in`.SurveyOptionResult
import com.moyeobus.application.survey.port.`in`.SurveyOptionUseCase
import com.moyeobus.global.response.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/surveys")
class SurveyController(
    private val surveyAnswerUseCase: SurveyAnswerUseCase,
    private val surveyOptionUseCase: SurveyOptionUseCase
) : SurveyControllerDocs{

    @GetMapping
    override fun queryAll() : ResponseEntity<ApiResponse<SurveyOptionResult>> {
        return ResponseEntity.ok(ApiResponse.onSuccess(surveyOptionUseCase.queryAll()))
    }

    @PostMapping
    override fun create(
        @Valid @RequestBody command: SurveyCommand
    ): ResponseEntity<ApiResponse<Void>> {
        surveyAnswerUseCase.create(command)
        return ResponseEntity.ok(ApiResponse.onSuccessCreated())
    }
}