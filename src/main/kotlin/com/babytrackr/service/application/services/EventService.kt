package com.babytrackr.service.application.services

import com.babytrackr.service.application.mapper.EventMapper
import com.babytrackr.service.controller.model.request.CreateEventRequestDto
import com.babytrackr.service.controller.model.request.UpdateEventRequestDto
import com.babytrackr.service.controller.model.response.EventResponseDto
import com.babytrackr.service.controller.model.response.GetAllEventsResponseDto
import com.babytrackr.service.domain.enums.DiaperType
import com.babytrackr.service.domain.enums.EventType
import com.babytrackr.service.domain.model.Event
import com.babytrackr.service.domain.model.EventPayload.*
import com.babytrackr.service.domain.model.EventPayload
import com.babytrackr.service.infrastucture.repositories.EventRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val babyFinder: BabyFinder,
    private val eventFinder: EventFinder,
    private val eventMapper: EventMapper,
) {

    fun createEvent(request: CreateEventRequestDto, babyId: Long): EventResponseDto {

        val persistedBaby = babyFinder.getBabyOrThrow(babyId)

        val mappedPayload = mapPayload(request.eventType, request.payload)

        val currentDate = Instant.now()
        val event = Event(
            id = null,
            babyId = babyId,
            eventType = request.eventType,
            payload = mappedPayload,
            previousPayload = null,
            createdOn = currentDate,
            modifiedOn = currentDate,
        )

        val eventEntity = eventMapper.toEntity(persistedBaby, event)

        val persistedEvent = eventRepository.save(eventEntity)

        val savedDomain = eventMapper.toDomain(persistedEvent)

        return eventMapper.toEventResponseDto(savedDomain)
    }

    fun getEvent(babyId: Long, eventId: Long): EventResponseDto {

        val persistedEvent = eventFinder.getEventOrThrow(babyId, eventId)

        val savedDomain = eventMapper.toDomain(persistedEvent)

        return eventMapper.toEventResponseDto(savedDomain)
    }

    fun getAllEvents(babyId: Long): GetAllEventsResponseDto {

        babyFinder.getBabyOrThrow(babyId)

        val persistedEvents = eventRepository.findByBabyId(babyId)
            .map { eventMapper.toDomain(it) }

        return GetAllEventsResponseDto(persistedEvents.map { eventMapper.toEventResponseDto(it) })
    }

    fun updateEvent(babyId: Long, eventId: Long, request: UpdateEventRequestDto): EventResponseDto {

        val persistedEvent = eventFinder.getEventOrThrow(babyId, eventId)

         val mappedPayload = mapPayload(persistedEvent.eventType, request.payload)

        val event = eventMapper.toDomain(persistedEvent)

        val updatedEvent = event.updatePayload(mappedPayload)

        val updatedPersistedEvent = eventMapper.toEntity(persistedEvent.baby, updatedEvent)

        val savedEntity = eventRepository.save(updatedPersistedEvent)

        val savedDomain = eventMapper.toDomain(savedEntity)

        return eventMapper.toEventResponseDto(savedDomain)
    }


    fun deleteEvent(babyId: Long, eventId: Long) {

        val event = eventFinder.getEventOrThrow(babyId, eventId)
        eventRepository.delete(event)
    }

    fun mapPayload(type: EventType, payload: Map<String, Any>): EventPayload {
        return when (type) {
            EventType.FEED -> FeedPayload(
                feedingAmount = (payload["feedingAmount"] as? Int)
                    ?: throw IllegalArgumentException("feedingAmount must be an Int"),
            )

            EventType.SLEEP -> SleepPayload(
                sleepDurationMin = (payload["sleepDurationMin"] as? Int)
                ?: throw IllegalArgumentException("sleepDurationMin must be an Int"),
            )

            EventType.DIAPER -> DiaperPayload(
                diaperType = DiaperType.entries.find {
                    it.name.equals(payload["diaperType"] as? String, ignoreCase = true)
                } ?: throw IllegalArgumentException("Invalid diaperType")
            )
        }
    }
}