package com.babytrackr.service.exception

import org.springframework.http.HttpStatus

class NotFoundException(
    code: ErrorCode,
    message: String
): ApiException(HttpStatus.NOT_FOUND, code, message)