package com.babytrackr.service.domain.model

import com.babytrackr.service.domain.enums.EventType
import java.time.Instant

data class Event(
    val id: Long?,
    val babyId: Long,
    val eventType: EventType,
    val payload: EventPayload,
    val version: String = "v1",
    val isCorrected: Boolean = false,
    val previousPayload: EventPayload? = null,
    val createdOn: Instant,
    val modifiedOn: Instant
) {
    init {
        when (eventType) {
            EventType.FEED -> require(payload is EventPayload.FeedPayload)
            EventType.SLEEP -> require(payload is EventPayload.SleepPayload)
            EventType.DIAPER -> require(payload is EventPayload.DiaperPayload)
            }
        }

    fun updatePayload(payload: EventPayload): Event {
        return this.copy(
            previousPayload = this.payload,
            payload = payload,
            isCorrected = true,
            modifiedOn = Instant.now()
        )

    }
}
