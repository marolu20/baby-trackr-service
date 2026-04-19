package com.babytrackr.service.controller.model.request

import jakarta.validation.constraints.NotEmpty

data class UpdateEventRequestDto(
    @field:NotEmpty(message = "payload required")
    val payload: Map<String, Any>
)