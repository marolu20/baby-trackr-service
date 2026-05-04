package com.babytrackr.service.exception

import org.springframework.http.HttpStatus

class BadRequestException(
    code: ErrorCode,
    message: String
): ApiException(HttpStatus.BAD_REQUEST, code, message)