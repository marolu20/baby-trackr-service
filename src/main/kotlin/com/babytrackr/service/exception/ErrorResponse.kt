package com.babytrackr.service.exception

data class ErrorResponse(
    val status: Int,
    val error: String,
    val code: ErrorCode,
    val message: String?
)