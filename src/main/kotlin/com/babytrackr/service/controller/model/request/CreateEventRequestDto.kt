package com.babytrackr.service.controller.model.request

import com.babytrackr.service.domain.enums.EventType
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class CreateEventRequestDto(
    @field:NotNull("eventType is required")
    val eventType: EventType,
    @field:NotEmpty("event payload is required")
    val payload: Map<String, Any>
)