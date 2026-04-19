package com.babytrackr.service.controller.model.request

import com.babytrackr.service.domain.enums.EventType

data class CreateEventRequestDto(
    val eventType: EventType,
    val payload: Map<String, Any>
)