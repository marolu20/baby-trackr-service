package com.babytrackr.service.controller.model.response

import com.babytrackr.service.domain.enums.EventType
import java.time.Instant

data class EventResponseDto(
    val id: Long,
    val eventType: EventType,
    val payload: Map<String, Any>,
    val version: String,
    val createdOn: Instant,
    val modifiedOn: Instant
)