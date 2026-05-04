package com.babytrackr.service.exception

import org.springframework.http.HttpStatus

open class ApiException(
    val status: HttpStatus,
    val code: ErrorCode,
    message: String // why doesnt this use val keyword?
): RuntimeException(message)