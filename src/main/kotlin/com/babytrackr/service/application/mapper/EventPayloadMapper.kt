package com.babytrackr.service.application.mapper

import com.babytrackr.service.domain.enums.EventType
import com.babytrackr.service.domain.model.EventPayload
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper

@Component
class EventPayloadMapper(
    private val objectMapper: ObjectMapper
) {

    // Domain -> Map (for Response)
    fun toMap(payload: EventPayload): Map<String, Any?> {
        return when (payload) {
            is EventPayload.FeedPayload -> mapOf(
                "feedingAmount" to payload.feedingAmount,
                "notes" to payload.notes,
                "eventTime" to payload.eventTime
            )

            is EventPayload.SleepPayload -> mapOf(
                "sleepDuration" to payload.sleepDurationMin,
                "notes" to payload.notes,
                "startTime" to payload.startTime,
                "endTime" to payload.endTime
            )

            is EventPayload.DiaperPayload -> mapOf(
                "diaperType" to payload.diaperType,
                "notes" to payload.notes,
                "eventTime" to payload.eventTime
            )
        }
    }

    // Domain -> String (for DB Entity)
    fun toJson(payload: EventPayload): String {
        return objectMapper.writeValueAsString(payload)
    }

    // String -> Domain (from DB Entity)
    fun fromJson(
        type: EventType,
        json: String
    ): EventPayload {
        return when (type) {
            EventType.FEED -> objectMapper.readValue(
                json,
                EventPayload.FeedPayload::class.java
            )

            EventType.SLEEP -> objectMapper.readValue(
                json,
                EventPayload.SleepPayload::class.java
            )

            EventType.DIAPER -> objectMapper.readValue(
                json,
                EventPayload.DiaperPayload::class.java
            )
        }
    }
}
