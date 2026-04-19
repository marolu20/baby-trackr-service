package com.babytrackr.service.domain.model

import com.babytrackr.service.domain.enums.DiaperType
import com.babytrackr.service.domain.enums.EventType
import java.time.Instant

data class Event(
    val id: Long?,
    val babyId: Long,
    val eventType: EventType,
    val payload: Map<String, Any>,
    val version: String = "v1",
    val isCorrected: Boolean = false,
    val previousPayload: Map<String, Any>? = null,
    val createdOn: Instant,
    val modifiedOn: Instant
) {
    init {
        when (eventType) {
            EventType.FEED -> validateFeedType()
            EventType.SLEEP -> validateSleepType()
            EventType.DIAPER -> validateDiaperType()
            }
        }

    private fun validateFeedType() {
        val feedingAmount = payload["feedingAmount"]
        require(feedingAmount is Int && feedingAmount > 0) {
            "Feed events must include feeding amount and greater than 0"
        }
    }

    private fun validateSleepType() {
        val sleepDuration = payload["sleepDurationMin"]
        require(sleepDuration is Int && sleepDuration > 0) {
            "Sleep events must include sleep duration and greater than 0" }
    }

    private fun validateDiaperType(){
        val diaperTypeValue = payload["diaperType"]
        require(diaperTypeValue is String) {
            "diaper must be String"
        }

        DiaperType.entries.find {
            it.name.equals(diaperTypeValue, ignoreCase = true)
        } ?: throw IllegalArgumentException("Invalid Diaper Type: $diaperTypeValue")
    }
}
