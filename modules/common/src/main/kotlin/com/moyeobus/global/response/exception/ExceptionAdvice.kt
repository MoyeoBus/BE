package com.moyeobus.global.response.exception

import com.moyeobus.global.response.ApiResponse
import com.moyeobus.global.response.ErrorDetail
import com.moyeobus.global.response.status.ErrorStatus
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*
import java.util.function.Consumer

@RestControllerAdvice(annotations = [RestController::class])
class ExceptionAdvice : ResponseEntityExceptionHandler() {
    private val log = KotlinLogging.logger { }

    // Bean Validation 제약 조건 위반 시 발생하는 예외를 처리
    @ExceptionHandler
    fun validation(e: ConstraintViolationException, request: WebRequest): ResponseEntity<Any>? {
        val errorMessage = e.constraintViolations.stream()
            .map { violation: ConstraintViolation<*> ->
                String.format(
                    "prop '%s' | val '%s' | msg %s",
                    violation.propertyPath,  // 위반된 필드 경로
                    violation.invalidValue,  // 유효하지 않은 값
                    violation.message // 제약 조건 위반 메시지
                )
            }
            .findFirst()
            .orElseThrow { RuntimeException("ConstraintViolationException 추출 도중 에러 발생") }

        return handleExceptionInternalConstraint(e, HttpHeaders.EMPTY, request, errorMessage)
    }

    // @Valid 어노테이션을 통한 검증 실패 시 발생하는 예외를 처리
    public override fun handleMethodArgumentNotValid(
        e: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errors: MutableMap<String, String> = LinkedHashMap()

        e.bindingResult.fieldErrors
            .forEach(Consumer { fieldError: FieldError ->
                val fieldName = fieldError.field
                val errorMessage = Optional.ofNullable(fieldError.defaultMessage)
                    .orElse("")
                errors.merge(
                    fieldName, errorMessage
                ) { existingErrorMessage: String, newErrorMessage: String ->
                    (existingErrorMessage + ", "
                            + newErrorMessage)
                }
            })

        return handleExceptionInternalArgs(
            e, HttpHeaders.EMPTY,
            ErrorStatus.valueOf("_BAD_REQUEST"), request, errors
        )
    }

    // 모든 Exception 클래스 타입의 예외 처리 (500번대)
    @ExceptionHandler
    fun exception(e: Exception, request: WebRequest): ResponseEntity<Any>? {
        val errorMessage = e.message
        val errorPoint = if (Objects.isNull(e.stackTrace))
            "No Stack Trace Error."
        else
            e.stackTrace[0].toString()
        return handleExceptionInternalFalse(
            e, ErrorStatus.INTERNAL_SERVER_ERROR,
            HttpHeaders.EMPTY, ErrorStatus.INTERNAL_SERVER_ERROR.httpStatus, request,
            e.message
        )
    }

    // 사용자 정의 예외 처리 (400번대)
    @ExceptionHandler(value = [GlobalException::class])
    fun onThrowException(
        globalException: GlobalException,
        request: HttpServletRequest
    ): ResponseEntity<*>? {
        val errorDetail = globalException.errorDetail
        return handleExceptionInternal(globalException, errorDetail, null, request)
    }

    private fun handleExceptionInternal(
        e: Exception, detail: ErrorDetail,
        headers: HttpHeaders?, request: HttpServletRequest
    ): ResponseEntity<Any>? {
        val body = ApiResponse.onFailure<Any?>(
            detail.code.toString(), detail.message.toString(),
            null
        )
        val webRequest: WebRequest = ServletWebRequest(request)

        return headers?.let {
            super.handleExceptionInternal(
                e,
                body,
                it,
                detail.httpStatus!!,
                webRequest
            )
        }
    }


    // 공통 예외 처리 메소드
    private fun handleExceptionInternalFalse(
        e: Exception,
        errorCommonStatus: ErrorStatus,
        headers: HttpHeaders, status: HttpStatus, request: WebRequest, errorPoint: String?
    ): ResponseEntity<Any>? {
        val body = ApiResponse.onFailure<Any?>(
            errorCommonStatus.code,
            errorCommonStatus.message, errorPoint
        )
        log.error(errorPoint)
        return super.handleExceptionInternal(
            e,
            body,
            headers,
            status,
            request
        )
    }

    // 서버 에러 처리 메소드
    private fun handleExceptionInternalArgs(
        e: Exception, headers: HttpHeaders,
        errorCommonStatus: ErrorStatus,
        request: WebRequest, errorArgs: Map<String, String>
    ): ResponseEntity<Any>? {
        val body = ApiResponse.onFailure<Any>(
            errorCommonStatus.code,
            errorCommonStatus.message, errorArgs
        )
        log.error(errorArgs.toString())
        return super.handleExceptionInternal(
            e,
            body,
            headers,
            errorCommonStatus.httpStatus,
            request
        )
    }

    // 검증 실패에 대한 처리 메소드
    private fun handleExceptionInternalConstraint(
        e: Exception,
        headers: HttpHeaders, request: WebRequest, message: String
    ): ResponseEntity<Any>? {
        val body = ApiResponse.onFailure<Any?>(
            ErrorStatus.BAD_REQUEST.code,
            message, null
        )
        log.error(message)
        return super.handleExceptionInternal(
            e,
            body,
            headers,
            ErrorStatus.BAD_REQUEST.httpStatus,
            request
        )
    }
}