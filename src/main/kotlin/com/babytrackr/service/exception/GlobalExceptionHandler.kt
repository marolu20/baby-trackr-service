package com.babytrackr.service.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    // handles all custom exceptions
    @ExceptionHandler(ApiException::class)
    fun handleApiException(
        ex: ApiException,
    ): ResponseEntity<ApiError> {
        val response = buildError(
            status = ex.status,
            code = ex.code,
            message = ex.message,
        )
        return ResponseEntity(response, ex.status)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(
        ex: MethodArgumentNotValidException,
    ): ResponseEntity<ApiError> {
        val message = ex.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }

        val status = HttpStatus.BAD_REQUEST
        val response =  buildError(
            status,
            ErrorCode.VALIDATION_ERROR,
            message,
        )
        return ResponseEntity(response, status)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(
        ex: Exception,
    ): ResponseEntity<ApiError> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        val response = buildError(
            status,
            ErrorCode.INTERNAL_ERROR,
            ex.message,
        )
        return ResponseEntity(response, status)
    }

    private fun buildError(
        status: HttpStatus,
        code: ErrorCode,
        message: String?,
    ): ApiError {
        return ApiError(
            status = status.value(),
            error = status.reasonPhrase,
            code = code,
            message = message,
        )
    }
}