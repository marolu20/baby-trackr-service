package com.babytrackr.service.unit.controller

import com.babytrackr.service.application.services.EventService
import com.babytrackr.service.controller.model.EventController
import com.babytrackr.service.controller.model.response.GetAllEventsResponseDto
import com.babytrackr.service.domain.enums.EventType
import com.babytrackr.service.exception.ErrorCode
import com.babytrackr.service.exception.GlobalExceptionHandler
import com.babytrackr.service.exception.NotFoundException
import com.babytrackr.service.fixtures.buildEventRequestDto
import com.babytrackr.service.fixtures.buildEventResponseDto
import com.babytrackr.service.fixtures.buildGetAllEventsResponseDto
import com.babytrackr.service.fixtures.buildUpdateEventRequestDto
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kotlin.test.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.verify

@WebMvcTest(controllers = [EventController::class, GlobalExceptionHandler::class])
class EventControllerUnitTest(
    @Autowired
    private val mockMvc: MockMvc
) {
    private val objectMapper = jacksonObjectMapper().findAndRegisterModules()

    @MockkBean
    lateinit var eventServiceMock: EventService

    @Test
    fun `should create new event`() {

        val id = 1L
        val babyId = 1L

        // given
        val request = buildEventRequestDto()

        val response = buildEventResponseDto(id, babyId)

        every { eventServiceMock.createEvent(any(), babyId) } returns response

        // when
        mockMvc.post("/v1/babies/{babyId}/events", babyId) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            // then
            .andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(id) }
                jsonPath("$.babyId") { value(babyId) }
                jsonPath("$.eventType") { value("FEED") }
                jsonPath("$.payload.feedingAmount") { value(5) }
            }
    }

    @Test
    fun `should get an event`() {

        val eventId = 1L
        val babyId = 1L

        // given
        val response = buildEventResponseDto(eventId, babyId)

        every { eventServiceMock.getEvent(babyId, eventId) } returns response

        // when
        mockMvc.get("/v1/babies/{babyId}/events/{eventId}", babyId, eventId) {
            accept = MediaType.APPLICATION_JSON
        }
            // then
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(eventId) }
                jsonPath("$.babyId") { value(babyId) }
                jsonPath("$.eventType") { value("FEED") }
                jsonPath("$.payload.feedingAmount") { value(5) }
            }
    }

    @Test
    fun `should get a list of events`() {
        val eventId = 1L
        val babyId = 1L

        // given
        val response = buildGetAllEventsResponseDto(eventId, babyId)

        every {
            eventServiceMock.getAllEvents(babyId)
        } returns response

        // when
        mockMvc.get("/v1/babies/{babyId}/events", babyId) {
            accept = MediaType.APPLICATION_JSON
        }
            // then
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.events.[0].id") { value(eventId) }
                jsonPath("$.events.[0].babyId") { value(babyId) }
                jsonPath("$.events.[0].eventType") { value("FEED") }
                jsonPath("$.events.[0].payload.feedingAmount") { value(5) }
            }
    }

    @Test
    fun `should update an event`() {
        val eventId = 1L
        val babyId = 1L

        // given
        val request = buildUpdateEventRequestDto(
            eventPayload = mapOf("feedingAmount" to 10),
        )
        val response = buildEventResponseDto(
            eventId,
            babyId,
            payload = mapOf("feedingAmount" to 10),
            isCorrected = true,
            previousPayload = mapOf("feedingAmount" to 5)
        )

        every { eventServiceMock.updateEvent(babyId, eventId, request) } returns response

        // when
        mockMvc.patch("/v1/babies/{babyId}/events/{eventId}", babyId, eventId) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            // then
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(eventId) }
                jsonPath("$.babyId") { value(babyId) }
                jsonPath("$.eventType") { value("FEED") }
                jsonPath("$.payload.feedingAmount") { value(10) }
                jsonPath("$.isCorrected") { value(true) }
            }
    }

    @Test
    fun `should delete an event`() {

        val eventId = 1L
        val babyId = 1L

        // given
        every { eventServiceMock.deleteEvent(babyId, eventId) } just runs

        // when
        mockMvc.delete("/v1/babies/{babyId}/events/{eventId}", babyId, eventId) {
            accept = MediaType.APPLICATION_JSON
        }
            // then
            .andExpect {
                status { isNoContent() }
            }
    }

    @Test
    fun `should return 404 if baby not found when retrieving an event`() {
        val babyId = 399L
        val eventId = 1L

        every {
            eventServiceMock.getEvent(babyId, eventId)
        } throws NotFoundException(
            ErrorCode.BABY_NOT_FOUND,"Could not find baby=${babyId}"
        )

        // when
        mockMvc.get("/v1/babies/{babyId}/events/{eventId}", babyId, eventId) {}
            // then
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun `should return 400 when event payload is empty when creating a new event`() {

        val babyId = 1L

        val request = mapOf(
            "eventType" to EventType.FEED,
            "payload" to mapOf<String, Any>()
        )

        // when
        mockMvc.post("/v1/babies/{babyId}/events", babyId) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            // then
            .andExpect {
                status { isBadRequest() }
            }
    }

    @Test
    fun `should return empty list if no events are found`(){
        val babyId = 10L

        // given
        every {
            eventServiceMock.getAllEvents(babyId)
        } returns GetAllEventsResponseDto(emptyList())

        //when
        mockMvc.get("/v1/babies/{babyId}/events", babyId) {
            accept = MediaType.APPLICATION_JSON
        }
            // then
            .andExpect {
                jsonPath("$.events.length()") { value((0)) }
            }
    }

    @Test
    fun `should call event service`() {

        val babyId = 10L
        val eventId = 1L

        // given
        val createRequest = buildEventRequestDto()
        val updateRequest = buildUpdateEventRequestDto(eventPayload = mapOf("feedingAmount" to 10),)
        val getResponse = buildEventResponseDto(eventId, babyId)

        val getAllResponse = buildGetAllEventsResponseDto(eventId, babyId)

        every {
            eventServiceMock.createEvent(any(), babyId)
        } returns getResponse

        every {
            eventServiceMock.getEvent(babyId, eventId)
        } returns getResponse

        every {
            eventServiceMock.getAllEvents(babyId)
        } returns getAllResponse

        every {
            eventServiceMock.updateEvent(babyId, eventId, any())
        } returns getResponse

        every {
            eventServiceMock.deleteEvent(babyId, eventId)
        } just runs

        // when
        mockMvc.post("/v1/babies/{babyId}/events", babyId) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createRequest)
            accept = MediaType.APPLICATION_JSON
        }

        mockMvc.get("/v1/babies/{babyId}/events/{eventId}", babyId, eventId) {
            accept = MediaType.APPLICATION_JSON
        }

        mockMvc.get("/v1/babies/{babyId}/events", babyId) {
            accept = MediaType.APPLICATION_JSON
        }

        mockMvc.patch("/v1/babies/{babyId}/events/{eventId}", babyId, eventId, updateRequest) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updateRequest)
            accept = MediaType.APPLICATION_JSON
        }

        mockMvc.delete("/v1/babies/{babyId}/events/{eventId}", babyId, eventId) {
            accept = MediaType.APPLICATION_JSON
        }

        // then
        verify(exactly = 1){ eventServiceMock.createEvent(any(), babyId) }
        verify(exactly = 1){ eventServiceMock.getEvent(babyId, eventId) }
        verify(exactly = 1){ eventServiceMock.getAllEvents(babyId) }
        verify(exactly = 1){ eventServiceMock.updateEvent(babyId, eventId, any()) }
        verify(exactly = 1) {eventServiceMock.deleteEvent(babyId, eventId)}
    }
}