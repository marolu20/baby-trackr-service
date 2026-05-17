package com.babytrackr.service.unit.service

import com.babytrackr.service.application.mapper.EventMapper
import com.babytrackr.service.application.services.BabyFinder
import com.babytrackr.service.application.services.EventFinder
import com.babytrackr.service.application.services.EventService
import com.babytrackr.service.domain.enums.EventType
import com.babytrackr.service.exception.ErrorCode
import com.babytrackr.service.exception.NotFoundException
import com.babytrackr.service.fixtures.buildBabyEntity
import com.babytrackr.service.fixtures.buildEvent
import com.babytrackr.service.fixtures.buildEventEntity
import com.babytrackr.service.fixtures.buildEventRequestDto
import com.babytrackr.service.fixtures.buildEventResponseDto
import com.babytrackr.service.fixtures.buildUpdateEventRequestDto
import com.babytrackr.service.infrastucture.repositories.EventRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.Test

@ExtendWith(MockKExtension::class)
class EventServiceUnitTest {

        @InjectMockKs
        lateinit var eventService: EventService

        @MockK
        lateinit var eventRepository: EventRepository

        @MockK
        lateinit var babyFinder: BabyFinder

        @MockK
        lateinit var eventFinder: EventFinder

        @MockK
        lateinit var eventMapper: EventMapper


        @Test
        fun `should create a event and return response`() {

            val babyId = 1L
            val eventId = 1L

            // given
            val request = buildEventRequestDto()
            val savedBabyEntity = buildBabyEntity(1L)
            val savedEventEntity = buildEventEntity(1L)
            val domainEvent = buildEvent(eventId, babyId)
            val expectedResponse = buildEventResponseDto(eventId, babyId)

            // stub the dependencies
            every { eventMapper.toEntity(any(), any()) } returns savedEventEntity

            every { eventMapper.toDomain(any()) } returns domainEvent

            every { babyFinder.getBabyOrThrow(1L) } returns savedBabyEntity

            every { eventRepository.save(any()) } returns savedEventEntity

            every { eventMapper.toEventResponseDto(any()) } returns expectedResponse

            // when
            val result = eventService.createEvent(request, 1L)

            // then
            assertEquals(1L, result.id)
            assertEquals(EventType.FEED, result.eventType)
            verify(exactly = 1) { babyFinder.getBabyOrThrow(1L) }
            verify(exactly = 1) { eventRepository.save(any()) }
        }

        @Test
        fun `should get an event and return response`() {

            val babyId = 1L
            val eventId = 1L

            // given
            val savedEntity = buildEventEntity()
            val domainEvent = buildEvent(eventId, babyId)
            val expectedResponse = buildEventResponseDto(eventId, babyId)

            // stub the dependencies
            every { eventFinder.getEventOrThrow(babyId, eventId) } returns savedEntity

            every { eventMapper.toDomain(any()) } returns domainEvent

            every { eventMapper.toEventResponseDto(any()) } returns expectedResponse

            // when
            val result = eventService.getEvent(babyId, eventId)

            // then
            assertEquals(1L, result.id)
            assertEquals(EventType.FEED, result.eventType)
            verify(exactly = 1) { eventFinder.getEventOrThrow(1L, 1L) }
        }

        @Test
        fun `should get a list of events and return response`() {

            val babyId = 1L
            val eventId = 1L

            // given
            val savedEntity = buildEventEntity(1L)
            val savedBabyEntity = buildBabyEntity(1L)
            val domainEvent = buildEvent(eventId, babyId)
            val expectedResponse = buildEventResponseDto(eventId, babyId)


            // stub the dependencies
            every { babyFinder.getBabyOrThrow(1L) } returns savedBabyEntity

            every { eventRepository.findByBabyId(any()) } returns listOf(savedEntity)

            every { eventMapper.toEventResponseDto(any()) } returns expectedResponse

            every { eventMapper.toDomain(any()) } returns domainEvent

            // when
            val result = eventService.getAllEvents(babyId)

            // then
            assertEquals(1L, result.events[0].id)
            assertEquals(EventType.FEED, result.events[0].eventType)
            verify(exactly = 1) { eventRepository.findByBabyId(any()) }
        }

        @Test
        fun `should update an event and return response`() {

            val babyId = 1L
            val eventId = 1L

            // given
            val request = buildUpdateEventRequestDto(
                eventPayload = mapOf("feedingAmount" to 10),
            )

            val domainEvent = buildEvent(eventId, babyId)
            val savedEntity = buildEventEntity(1L)
            val savedEventEntity = buildEventEntity(1L)
            val expectedResponse = buildEventResponseDto(
                id = eventId,
                babyId = babyId,
                payload = mapOf("feedingAmount" to 10),
                isCorrected = true
            )

            // stub the mock finder and repository dependencies
            every { eventFinder.getEventOrThrow(babyId, eventId) } returns savedEntity

            every { eventMapper.toDomain(any()) } returns domainEvent

            every { eventMapper.toEntity(any(), any()) } returns savedEventEntity

            every { eventRepository.save(any()) } returns savedEventEntity

            every { eventMapper.toEventResponseDto(any()) } returns expectedResponse

            // when
            val result = eventService.updateEvent(babyId, eventId, request)

            // then
            assertEquals(1L, result.id)
            assertEquals(mapOf("feedingAmount" to 10), result.payload)
            assertEquals(true, result.isCorrected)
            verify(exactly = 1) { eventRepository.save(any()) }
            verify(exactly = 1) { eventFinder.getEventOrThrow(babyId, eventId) }
        }

        @Test
        fun `should delete an event and not return response`() {

            val babyId = 1L
            val eventId = 55L

            // given
            val savedEntity = buildEventEntity(1L)

            // stub the dependencies
            every { eventFinder.getEventOrThrow(babyId, eventId) } returns savedEntity

            every { eventRepository.delete(savedEntity) } just runs

            // when
            eventService.deleteEvent(babyId, eventId)

            // then
            verify(exactly = 1) { eventFinder.getEventOrThrow(babyId, eventId) }
            verify(exactly = 1) { eventRepository.delete(savedEntity) }
        }

    @Test
    fun `should throw error if baby is not found`() {

        val babyId = 10L
        val request = buildEventRequestDto()

        // given
        every { babyFinder.getBabyOrThrow(babyId) } throws NotFoundException(
            ErrorCode.BABY_NOT_FOUND,
            "Could not find babyid=$babyId"
        )

        // when
        val exception = assertThrows<NotFoundException> {
            eventService.createEvent(request, babyId)
        }

        // then
        assertEquals(ErrorCode.BABY_NOT_FOUND, exception.code)

        verify(exactly = 1) { babyFinder.getBabyOrThrow(babyId) }
    }

    @Test
    fun `should throw error if event is not found`() {

        val babyId = 1L
        val eventId = 10L

        // given
        val savedEntity = buildBabyEntity(1L)

        every { babyFinder.getBabyOrThrow(babyId) } returns savedEntity
        every { eventFinder.getEventOrThrow(babyId, eventId) } throws NotFoundException(
            ErrorCode.EVENT_NOT_FOUND,
            "Could not find event=${eventId} with babyid=$babyId"
        )

        // when
        val exception = assertThrows<NotFoundException> {
            eventService.getEvent(babyId, eventId)
        }

        // then
        assertEquals(ErrorCode.EVENT_NOT_FOUND, exception.code)
        verify(exactly = 1) { eventFinder.getEventOrThrow(babyId, eventId) }
    }

    @Test
    fun `should throw error if repository save fails`() {

        val babyId = 1L

        // given
        val request = buildEventRequestDto()

        // stub the repository dependency
        every { babyFinder.getBabyOrThrow(any()) } throws RuntimeException("Database connection timeout")

        // when
        val exception = assertThrows<RuntimeException> {
            eventService.createEvent(request, babyId)
        }

        // then
        assertEquals("Database connection timeout", exception.message)
        verify(exactly = 0) { eventRepository.save(any()) }
    }

    @Test
    fun `should throw error if feed payload amount is zero or less`(){

        val babyId = 1L

        // given
        val request = buildEventRequestDto(
            eventType = EventType.FEED,
            eventPayload = mapOf("feedingAmount" to 0)
        )

        val savedBabyEntity = buildBabyEntity(1L)

        every { babyFinder.getBabyOrThrow(any()) } returns savedBabyEntity

        // when
        val exception = assertThrows<IllegalArgumentException> {
            eventService.createEvent(request, babyId)
        }

        // then
        assertEquals("Feed events must include feeding amount and greater than 0", exception.message)

        verify(exactly = 0) { eventRepository.save(any()) }
    }

    @Test
    fun `should throw error if sleep payload amount is zero or less`(){

        val babyId = 1L

        // given
        val request = buildEventRequestDto(
            eventType = EventType.SLEEP,
            eventPayload = mapOf("sleepDurationMin" to 0)
        )

        val savedBabyEntity = buildBabyEntity(1L)

        every { babyFinder.getBabyOrThrow(any()) } returns savedBabyEntity

        // when
        val exception = assertThrows<IllegalArgumentException> {
            eventService.createEvent(request, babyId)
        }

        // then
        assertEquals("Sleep events must include sleep duration and greater than 0", exception.message)

        verify(exactly = 0) { eventRepository.save(any()) }
    }
}
