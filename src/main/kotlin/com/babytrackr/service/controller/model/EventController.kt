package com.babytrackr.service.controller.model

import com.babytrackr.service.controller.model.request.CreateEventRequestDto
import com.babytrackr.service.controller.model.request.UpdateEventRequestDto
import com.babytrackr.service.controller.model.response.EventResponseDto
import com.babytrackr.service.controller.model.response.GetAllEventsResponseDto
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
@RequestMapping("/v1/babies/{babyId}/events")
class EventController {

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun createEvent(
        @PathVariable("babyId") babyId: Long,
        @RequestBody @Valid request: CreateEventRequestDto
    ): EventResponseDto {
        return // Call Service
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    fun retrieveEvent(
        @PathVariable("babyId") babyId: Long,
        @PathVariable("eventId") eventId: Long,
    ): EventResponseDto {
        return // Call Service
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    fun retrieveAllEvents(
        @PathVariable("babyId") babyId: Long,
    ): GetAllEventsResponseDto {
        return // Call Service
    }

    @PatchMapping("{eventId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateEvent(
        @PathVariable("babyId") babyId: Long,
        @PathVariable("eventId") eventId: Long,
        @RequestBody @Valid request: UpdateEventRequestDto
    ): EventResponseDto {
        return // Call Service
    }

    @DeleteMapping("{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteEvent(
        @PathVariable("babyId") babyId: Long,
        @PathVariable("eventId") eventId: Long,
    ) {
    }
}

