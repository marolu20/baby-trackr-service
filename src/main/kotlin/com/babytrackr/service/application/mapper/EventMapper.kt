package com.babytrackr.service.application.mapper

import com.babytrackr.service.application.util.toTitleCase
import com.babytrackr.service.controller.model.response.ActivityResponse
import com.babytrackr.service.controller.model.response.EventResponseDto
import com.babytrackr.service.domain.enums.EventType
import com.babytrackr.service.domain.model.ActivityQueryResult
import com.babytrackr.service.domain.model.Event
import com.babytrackr.service.domain.model.EventPayload
import com.babytrackr.service.infrastucture.repositories.BabyEntity
import com.babytrackr.service.infrastucture.repositories.EventEntity
import org.springframework.stereotype.Component

@Component
class EventMapper(
    private val payloadMapper: EventPayloadMapper,
    private val EVENT_TITLES: Map<EventType, String> = mapOf(
        EventType.FEED to "Feeding",
        EventType.SLEEP to "Sleep",
        EventType.DIAPER to "Diaper"
    )
) {

    //Domain to Entity
    fun toEntity(
        baby: BabyEntity,
        event: Event
    ): EventEntity {
        return EventEntity(
            id = event.id,
            eventType = event.eventType,
            eventTime = event.eventTime,
            payload = payloadMapper.toJson(event.payload),
            isCorrected = event.isCorrected,
            previousPayload = event.previousPayload?.let {
                payloadMapper.toJson(it)
            },
            createdOn = event.createdOn,
            modifiedOn = event.modifiedOn,
            version = event.version,
            baby = baby
        )
    }

    // Entity to Domain
    fun toDomain(entity: EventEntity): Event {
        return Event(
            id = entity.id,
            babyId = entity.baby.id!!,
            eventType = entity.eventType,
            eventTime = entity.eventTime,
            payload = payloadMapper.fromJson(entity.eventType, entity.payload),
            version = entity.version,
            isCorrected = entity.isCorrected,
            previousPayload = entity.previousPayload?.let {
                payloadMapper.fromJson(entity.eventType, it)
            },
            createdOn = entity.createdOn,
            modifiedOn = entity.modifiedOn
        )
    }

    //Domain to Response DTO
    fun toEventResponseDto(event: Event): EventResponseDto {
        return EventResponseDto(
            id = event.id!!,
            babyId = event.babyId,
            eventType = event.eventType,
            eventTime = event.eventTime,
            payload = payloadMapper.toMap(event.payload),
            isCorrected = event.isCorrected,
            previousPayload = event.previousPayload?.let {
                payloadMapper.toMap(it)
            },
            version = "v1",
            createdOn = event.createdOn,
            modifiedOn = event.modifiedOn
        )
    }

    fun toActivityResponseDto(queryResult: ActivityQueryResult): ActivityResponse {
        val description = when (val payload = queryResult.payload) {
            is EventPayload.FeedPayload -> "${payload.feedingAmount} oz"
            is EventPayload.SleepPayload -> {formatDuration(payload.sleepDurationMin)}
            is EventPayload.DiaperPayload -> "${toTitleCase(payload.diaperType.toString())} diaper"
        }

        val notes = when (val payload = queryResult.payload) {
            is EventPayload.FeedPayload -> payload.notes
            is EventPayload.SleepPayload -> payload.notes
            is EventPayload.DiaperPayload -> payload.notes
        }
        return ActivityResponse(
            eventId = queryResult.eventId,
            type = queryResult.eventType,
            title = EVENT_TITLES.getValue(queryResult.eventType),
            description = description,
            babyFirstName = queryResult.babyFirstName,
            notes = notes,
            timestamp = queryResult.eventTime
        )
    }

    private fun formatDuration(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60

        return when {
            hours == 0 -> "$remainingMinutes min"
            remainingMinutes == 0 -> "$hours hr"
            else -> "$hours hr $remainingMinutes min"
        }
    }

}
