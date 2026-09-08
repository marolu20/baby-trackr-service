package com.babytrackr.service.controller.model.response

import com.babytrackr.service.domain.enums.EventType
import java.time.Instant

data class ActivityResponse(
    val eventId: Long,
    val type: EventType,
    val title: String,
    val description: String,
    val babyFirstName: String,
    val notes: String?,
    val timestamp: Instant,
)
