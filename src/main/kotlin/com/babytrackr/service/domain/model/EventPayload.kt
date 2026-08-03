package com.babytrackr.service.domain.model

import com.babytrackr.service.domain.enums.DiaperType
import java.time.Instant

sealed class EventPayload {

    data class FeedPayload(
        val feedingAmount: Int,
        val notes: String? = null,
        val eventTime: Instant,
    ): EventPayload() {

        init {
            require(feedingAmount > 0) {
                "Feed events must include feeding amount and greater than 0"
            }
        }
    }

    data class SleepPayload(
        val sleepDurationMin: Int,
        val notes: String? = null,
        val startTime: Instant,
        val endTime: Instant
    ): EventPayload() {

        init {
            require(sleepDurationMin > 0) {
                "Sleep events must include sleep duration and greater than 0" }
        }
    }

    data class DiaperPayload(
        val diaperType: DiaperType,
        val notes: String? = null,
        val eventTime: Instant,
    ): EventPayload()
}
