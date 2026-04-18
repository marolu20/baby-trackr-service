package com.babytrackr.service.controller.model.request

enum class EventType {
    FEED,
    SLEEP,
    DIAPER
}

enum class DiaperType {
    LIQUID,
    SOLID
}
data class CreateEventRequestDto(
    val eventType: EventType,
    val payload: Map<String, Any>
)