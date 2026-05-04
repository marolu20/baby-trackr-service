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
        ex: NotFoundException, //what is this?
    ): ResponseEntity<ApiError> {
        return buildError(
            status = ex.status,
            code = ex.code,
            message = ex.message,
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(
        ex: MethodArgumentNotValidException,
    ): ResponseEntity<ApiError> {
        val message = ex.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        return buildError(
            HttpStatus.BAD_REQUEST,
            ErrorCode.VALIDATION_ERROR,
            message,
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(
        ex: Exception,
    ): ResponseEntity<ApiError> {
        return buildError(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.INTERNAL_ERROR,
            ex.message,
        )
    }

    private fun buildError(
        status: HttpStatus,
        code: ErrorCode,
        message: String?,
    ): ResponseEntity<ApiError> {
        val error = ApiError(
            status = status.value(),
            error = status.reasonPhrase,
            code = code,
            message = message,
        )
        return ResponseEntity.status(status).body(error)
    }
}