package com.babytrackr.service.domain.model

import com.babytrackr.service.controller.model.request.DiaperType
import com.babytrackr.service.controller.model.request.EventType
import java.time.Instant

data class Event(
    val id: String,
    val eventType: EventType,
    val payload: Map<String, Any>,
    val createdOn: Instant,
    val modifiedOn: Instant
) {
    init {
        when (eventType) {
            EventType.FEED -> {
                val feedingAmount = payload["feedingAmount"]
                require(feedingAmount is Int && feedingAmount > 0) {
                    "Feed events must include feeding amount and greater than 0"
                }
            }

            EventType.SLEEP -> {
                val sleepDuration = payload["sleepDurationMin"]
                require(sleepDuration is Int && sleepDuration > 0) {
                    "Sleep events must include sleep duration and greater than 0" }
                }

            EventType.DIAPER -> {
                val diaperTypeValue = payload["diaperType"]
                require(diaperTypeValue is String) {
                    "diaper must be String"
                }

                val diaperType = DiaperType.entries.find {
                    it.name.equals(diaperTypeValue, ignoreCase = true)
                } ?: throw IllegalArgumentException("Invalid Diaper Type: $diaperTypeValue")
            }
        }

    }
}