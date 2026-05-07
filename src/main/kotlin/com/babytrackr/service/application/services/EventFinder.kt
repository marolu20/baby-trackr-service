package com.babytrackr.service.application.services

import com.babytrackr.service.exception.ErrorCode
import com.babytrackr.service.exception.NotFoundException
import com.babytrackr.service.infrastucture.repositories.EventEntity
import com.babytrackr.service.infrastucture.repositories.EventRepository
import org.springframework.stereotype.Component

@Component
class EventFinder(
    private val eventRepository: EventRepository,
) {
    fun getEventOrThrow(eventId: Long, babyId: Long): EventEntity {
        return eventRepository.findByIdAndBabyId(babyId, eventId)
            ?: throw NotFoundException(
                ErrorCode.EVENT_NOT_FOUND,
                "Could not find event=${eventId} with babyid=$babyId"
            )
    }
}