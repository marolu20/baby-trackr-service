package com.babytrackr.service.controller.model.response

import com.babytrackr.service.controller.model.request.EventType
import java.time.Instant

data class EventResponseDto(
    val id: Long,
    val eventType: EventType,
    val payload: Map<String, Any>,
    val createdOn: Instant,
    val modifiedOn: Instant
)