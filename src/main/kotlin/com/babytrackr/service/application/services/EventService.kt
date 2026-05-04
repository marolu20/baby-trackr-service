package com.babytrackr.service.application.services

import com.babytrackr.service.application.mapper.toDomain
import com.babytrackr.service.application.mapper.toEntity
import com.babytrackr.service.application.mapper.toEventResponseDto
import com.babytrackr.service.controller.model.request.CreateEventRequestDto
import com.babytrackr.service.controller.model.request.UpdateEventRequestDto
import com.babytrackr.service.controller.model.response.EventResponseDto
import com.babytrackr.service.controller.model.response.GetAllEventsResponseDto
import com.babytrackr.service.domain.model.Event
import com.babytrackr.service.infrastucture.repositories.EventRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val babyFinder: BabyFinder,
    private val eventFinder: EventFinder
) {

    fun createEvent(request: CreateEventRequestDto, babyId: Long): EventResponseDto {

        val persistedBaby = babyFinder.getBabyOrThrow(babyId)

        val currentDate = Instant.now()
        val event = Event(
            id = null,
            babyId = babyId,
            eventType = request.eventType,
            payload = request.payload,
            previousPayload = null,
            createdOn = currentDate,
            modifiedOn = currentDate,
        )

        val eventEntity = event.toEntity(persistedBaby)

        val persistedEvent = eventRepository.save(eventEntity)

        val savedDomain = persistedEvent.toDomain()

        return savedDomain.toEventResponseDto()
    }

    fun getEvent(babyId: Long, eventId: Long): EventResponseDto {

        val persistedEvent = eventFinder.getEventOrThrow(babyId, eventId)

        val savedDomain = persistedEvent.toDomain()

        return savedDomain.toEventResponseDto()
    }

    fun getEvents(babyId: Long): GetAllEventsResponseDto {

        babyFinder.getBabyOrThrow(babyId)

        val persistedEvents = eventRepository.findByBabyId(babyId)
            .map { it.toDomain() }

        return GetAllEventsResponseDto(persistedEvents.map { it.toEventResponseDto() })
    }

    fun updateEvent(babyId: Long, eventId: Long, request: UpdateEventRequestDto): EventResponseDto {

        val persistedEvent = eventFinder.getEventOrThrow(babyId, eventId)

        val event = persistedEvent.toDomain()

        val updatedEvent = event.updatePayload(request.payload)

        val updatedPersistedEvent = updatedEvent.toEntity(persistedEvent.baby)

        val savedEntity = eventRepository.save(updatedPersistedEvent)

        val savedDomain = savedEntity.toDomain()

        return savedDomain.toEventResponseDto()
    }


    fun deleteEvent(babyId: Long, eventId: Long) {

        val event = eventFinder.getEventOrThrow(babyId, eventId)
        eventRepository.delete(event)
    }
}