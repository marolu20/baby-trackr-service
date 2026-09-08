package com.babytrackr.service.controller.model.response

data class PaginationResponse<T>(
    val data: List<T>,
    val page: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalItems: Long
)
