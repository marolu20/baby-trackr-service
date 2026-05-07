package com.babytrackr.service.application.mapper

import com.babytrackr.service.controller.model.response.EventResponseDto
import com.babytrackr.service.domain.model.Event
import com.babytrackr.service.infrastucture.repositories.BabyEntity
import com.babytrackr.service.infrastucture.repositories.EventEntity
import org.springframework.stereotype.Component

@Component
class EventMapper(
    private val payloadMapper: EventPayloadMapper,
) {

    //Domain to Entity
    fun toEntity(
        baby: BabyEntity,
        event: Event
    ): EventEntity {
        return EventEntity(
            id = event.id,
            eventType = event.eventType,
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
}