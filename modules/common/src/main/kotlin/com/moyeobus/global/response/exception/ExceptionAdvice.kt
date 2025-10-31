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
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.HttpRequestMethodNotSupportedException
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


    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        val path = (request as ServletWebRequest).request.requestURI
        val message = ex.mostSpecificCause?.message ?: "요청 본문을 읽을 수 없습니다."
        val body = ApiResponse.onFailure<Any>(
            ErrorStatus.BAD_REQUEST.code,
            ErrorStatus.BAD_REQUEST.message,
            message, path
        )
        return handleExceptionInternal(ex, body, headers, ErrorStatus.BAD_REQUEST.httpStatus, request)!!
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

    override fun handleHttpRequestMethodNotSupported(
        ex: HttpRequestMethodNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        val servletRequest = (request as ServletWebRequest).request
        val path = servletRequest.requestURI

        val body = ApiResponse.onFailure(
            code = ErrorStatus.METHOD_NOT_ALLOWED.code,
            message = ErrorStatus.METHOD_NOT_ALLOWED.message,
            data = ex.message ?: "허용되지 않은 HTTP 메서드입니다.",
            requestUri = path
        )

        return ResponseEntity(body, ErrorStatus.METHOD_NOT_ALLOWED.httpStatus)
    }

    private fun handleExceptionInternal(
        e: Exception, detail: ErrorDetail,
        headers: HttpHeaders?, request: HttpServletRequest
    ): ResponseEntity<Any>? {
        val body = ApiResponse.onFailure<Any?>(
            detail.code.toString(), detail.message.toString(),
            null, request.requestURI
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
        val path = (request as ServletWebRequest).request.requestURI
        val body = ApiResponse.onFailure<Any?>(
            errorCommonStatus.code,
            errorCommonStatus.message, errorPoint,
            path
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
        val path = (request as ServletWebRequest).request.requestURI
        val body = ApiResponse.onFailure<Any>(
            errorCommonStatus.code,
            errorCommonStatus.message, errorArgs,
            path
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
        val path = (request as ServletWebRequest).request.requestURI
        val body = ApiResponse.onFailure<Any?>(
            ErrorStatus.BAD_REQUEST.code,
            message, null,
            path
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