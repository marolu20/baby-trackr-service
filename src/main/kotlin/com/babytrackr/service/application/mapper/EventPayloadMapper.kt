package com.babytrackr.service.application.mapper

import com.babytrackr.service.domain.enums.DiaperType
import com.babytrackr.service.domain.enums.EventType
import com.babytrackr.service.domain.model.EventPayload
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Component
class EventPayloadMapper(
    private val objectMapper: ObjectMapper
) {

    // Map -> Domain
    fun fromMap(type: EventType, payload: Map<String, Any>): EventPayload {
        return when (type) {
            EventType.FEED -> EventPayload.FeedPayload(
                feedingAmount = payload["feedingAmount"] as Int,
            )

            EventType.SLEEP -> EventPayload.SleepPayload(
                sleepDurationMin = payload["sleepDurationMin"] as Int,
            )

            EventType.DIAPER -> {
                val diaperTypeValue = payload["diaperType"] as? String
                ?: throw IllegalArgumentException("diaperType must be a String")

                EventPayload.DiaperPayload(
                    diaperType = DiaperType.valueOf(diaperTypeValue.uppercase())
                )
            }
        }
    }

    // Domain -> Map (for Response)
    fun toMap(payload: EventPayload): Map<String, Any> {
        return when (payload) {
            is EventPayload.FeedPayload -> mapOf(
                "feedingAmount" to payload.feedingAmount
            )

            is EventPayload.SleepPayload -> mapOf(
                "sleepDuration" to payload.sleepDurationMin
            )

            is EventPayload.DiaperPayload -> mapOf(
                "diaperType" to payload.diaperType,
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
        val map: Map<String, Any> =
            objectMapper.readValue(json, Map::class.java) as Map<String, Any>
        return fromMap(type, map)
    }
}