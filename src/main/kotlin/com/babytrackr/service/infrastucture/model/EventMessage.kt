package com.babytrackr.service.infrastucture.model

import com.babytrackr.service.domain.enums.EventType
import com.babytrackr.service.domain.enums.OperationType
import java.time.Instant

data class EventMessage(
    val eventId: Long,
    val babyId: Long,
    val userId: Long?,
    val eventType: EventType,
    val operationType: OperationType,
    val payload: String,
    val createdAt: Instant
)
