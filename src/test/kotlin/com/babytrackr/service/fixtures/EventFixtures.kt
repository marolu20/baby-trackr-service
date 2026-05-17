package com.babytrackr.service.fixtures

import com.babytrackr.service.controller.model.request.CreateEventRequestDto
import com.babytrackr.service.controller.model.request.UpdateEventRequestDto
import com.babytrackr.service.controller.model.response.EventResponseDto
import com.babytrackr.service.controller.model.response.GetAllEventsResponseDto
import com.babytrackr.service.domain.enums.EventType
import com.babytrackr.service.domain.model.Event
import com.babytrackr.service.domain.model.EventPayload
import com.babytrackr.service.infrastucture.repositories.EventEntity
import java.time.Instant

fun buildEventRequestDto(
    eventType: EventType = EventType.FEED,
    eventPayload: Map<String, Any>? = mapOf("feedingAmount" to 5)
): CreateEventRequestDto {
    return CreateEventRequestDto(
        eventType = eventType,
        payload = eventPayload ?: mapOf("feedingAmount" to 5)
    )
}

fun buildGetAllEventsResponseDto(
    id: Long,
    babyId: Long,
): GetAllEventsResponseDto {
    val events = buildEventResponseDto(id,babyId)
    return GetAllEventsResponseDto(listOf(events))
}

fun buildUpdateEventRequestDto(
    eventPayload: Map<String, Any>?
): UpdateEventRequestDto {
    return UpdateEventRequestDto(
        payload = eventPayload ?: mapOf("feedingAmount" to 5),
    )
}

fun buildEvent(
    id: Long,
    babyId: Long,
    eventType: EventType = EventType.FEED,
    payload: EventPayload = EventPayload.FeedPayload(feedingAmount = 5),
    version: String = "v1",
    isCorrected: Boolean = false,
    previousPayload: EventPayload? = null,
    createdOn: Instant = Instant.now(),
    modifiedOn: Instant = Instant.now(),
): Event {
    return Event(
        id = id,
        babyId = babyId,
        eventType = eventType,
        payload = payload,
        version = version,
        isCorrected = isCorrected,
        previousPayload = previousPayload,
        createdOn = createdOn,
        modifiedOn = modifiedOn,
    )
}

fun buildEventEntity(
    id: Long? = null,
    babyId: Long = 1L,
    eventType: EventType = EventType.FEED,
    payload: String = """{"feedingAmount": 5}""",
    version: String = "v1",
    isCorrected: Boolean = false,
    previousPayload: String? = null,
    createdOn: Instant = Instant.now(),
    modifiedOn: Instant = Instant.now(),
): EventEntity {
    return EventEntity(
        id = id,
        eventType = eventType,
        payload = payload,
        version = version,
        isCorrected = isCorrected,
        previousPayload = previousPayload,
        createdOn = createdOn,
        modifiedOn = modifiedOn,
        baby = buildBabyEntity(babyId)
    )
}

fun buildEventResponseDto(
    id: Long,
    babyId: Long,
    eventType: EventType = EventType.FEED,
    payload: Map<String, Any> = mapOf("feedingAmount" to 5),
    version: String = "v1",
    isCorrected: Boolean = false,
    previousPayload: Map<String, Any>? = null,
    createdOn: Instant = Instant.now(),
    modifiedOn: Instant = Instant.now(),
): EventResponseDto {
    return EventResponseDto(
        id = id,
        babyId = babyId,
        eventType = eventType,
        payload = payload,
        version = version,
        isCorrected = isCorrected,
        previousPayload = previousPayload,
        createdOn = createdOn,
        modifiedOn = modifiedOn
    )
}