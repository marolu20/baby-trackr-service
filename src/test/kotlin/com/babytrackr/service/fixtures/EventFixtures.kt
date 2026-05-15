package com.babytrackr.service.fixtures

import com.babytrackr.service.controller.model.request.CreateEventRequestDto
import com.babytrackr.service.controller.model.request.UpdateEventRequestDto
import com.babytrackr.service.controller.model.response.EventResponseDto
import com.babytrackr.service.controller.model.response.GetAllEventsResponseDto
import com.babytrackr.service.domain.enums.EventType
import java.time.Instant

fun buildEventRequestDto(
    eventType: EventType? = EventType.FEED,
    eventPayload: Map<String, Any>? = mapOf("feedingAmount" to 5)
): CreateEventRequestDto {
    return CreateEventRequestDto(
        eventType = eventType ?: EventType.FEED,
        payload = eventPayload ?: mapOf("feedingAmount" to 5)
    )
}

fun buildEventResponseDto(
    id: Long,
    babyId: Long,
    eventType: EventType? = EventType.FEED,
    eventPayload: Map<String, Any>? = mapOf("feedingAmount" to 5),
    isCorrected: Boolean? = false,
    previousPayload: Map<String, Any>? = null,
): EventResponseDto {
    return EventResponseDto(
        id = id,
        babyId = babyId,
        eventType = eventType ?: EventType.FEED,
        payload = eventPayload ?: mapOf("feedingAmount" to 5),
        isCorrected = isCorrected ?: false,
        previousPayload = previousPayload,
        version = "v1",
        createdOn = Instant.now(),
        modifiedOn = Instant.now()
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