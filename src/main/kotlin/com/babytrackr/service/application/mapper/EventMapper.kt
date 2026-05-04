package com.babytrackr.service.application.mapper

import com.babytrackr.service.controller.model.response.EventResponseDto
import com.babytrackr.service.domain.model.Event
import com.babytrackr.service.infrastucture.repositories.BabyEntity
import com.babytrackr.service.infrastucture.repositories.EventEntity

//Domain to Entity
fun Event.toEntity(baby: BabyEntity): EventEntity {
    return EventEntity(
        id = id,
        eventType = eventType,
        payload = payload,
        isCorrected = isCorrected,
        previousPayload = previousPayload,
        createdOn = createdOn,
        modifiedOn = modifiedOn,
        version = version,
        baby = baby
    )
}

// Entity to Domain
fun EventEntity.toDomain(): Event {
    return Event(
        id = id,
        babyId = baby.id!!,
        eventType = eventType,
        payload = payload,
        version = version,
        isCorrected = isCorrected,
        previousPayload = previousPayload,
        createdOn = createdOn,
        modifiedOn = modifiedOn
    )
}

//Domain to Response DTO
fun Event.toEventResponseDto(): EventResponseDto {
    return EventResponseDto(
        id = id ?: throw IllegalStateException("Id cannot be null"),
        babyId = babyId,
        eventType = eventType,
        payload = payload,
        isCorrected = false,
        previousPayload = null,
        version = "v1",
        createdOn = createdOn,
        modifiedOn = modifiedOn
    )
}
