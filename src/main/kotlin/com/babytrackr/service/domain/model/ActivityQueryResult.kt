package com.babytrackr.service.domain.model

import com.babytrackr.service.domain.enums.EventType
import java.time.Instant

data class ActivityQueryResult(
    val eventId: Long,
    val babyId: Long,
    val babyFirstName: String,
    val eventType: EventType,
    val eventTime: Instant,
    val payload: EventPayload,
    val createdOn: Instant,
    val modifiedOn: Instant
)
