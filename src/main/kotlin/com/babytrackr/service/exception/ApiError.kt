package com.babytrackr.service.exception

data class ApiError(
    val status: Int,
    val error: String,
    val code: ErrorCode,
    val message: String?,
)