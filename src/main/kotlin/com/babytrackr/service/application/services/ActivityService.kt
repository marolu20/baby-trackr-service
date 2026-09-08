package com.babytrackr.service.application.services

import com.babytrackr.service.application.mapper.EventMapper
import com.babytrackr.service.application.mapper.EventPayloadMapper
import com.babytrackr.service.controller.model.request.ActivityFilter
import com.babytrackr.service.controller.model.request.ActivityPagination
import com.babytrackr.service.controller.model.response.ActivityResponse
import com.babytrackr.service.controller.model.request.ActivitySortDirection
import com.babytrackr.service.controller.model.request.ActivitySortField
import com.babytrackr.service.domain.model.ActivityQueryResult
import com.babytrackr.service.infrastucture.repositories.ActivitySpecification
import com.babytrackr.service.infrastucture.repositories.EventRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ModelAttribute

@Service
class ActivityService(
    private val eventRepository: EventRepository,
    private val eventMapper: EventMapper,
    private val payloadMapper: EventPayloadMapper
) {

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(EventService::class.java)
    }


    fun getActivityReport(
        filter: ActivityFilter,
        @ModelAttribute pagination: ActivityPagination
    ): Page<ActivityResponse> {

        val specs = ActivitySpecification(filter).build()

        // Safely resolve parameters from the pagination data class instance
        // Safe 1-based check: If null or less than 1, default to 1-based first page (which is 0 in Spring)
        val incomingPage = if (pagination.page == null || pagination.page < 1) 1 else pagination.page

        val targetPage = incomingPage - 1
        val targetSize = pagination.pageSize ?: 20 // Fallback to a default page size if null

        // Map the custom Sort Direction enum to Spring Data's Direction enum
        val springDirection = if (pagination.sortBy.direction == ActivitySortDirection.ASCENDING) {
            Sort.Direction.ASC
        } else {
            Sort.Direction.DESC
        }

        val databaseFieldName = when (pagination.sortBy.field) {
            ActivitySortField.TIMESTAMP -> "eventTime"
            ActivitySortField.TYPE -> "eventType"
        }

        val pagination: Pageable = PageRequest.of(
                targetPage,
                targetSize,
                Sort.by(springDirection, databaseFieldName)
            )

        // Query the database
        val eventPage = eventRepository.findAll(specs, pagination)

        return eventPage.map { entity ->
            // Convert the raw database payload string into the EventPayload class type
            val parsedPayload = payloadMapper.fromJson(
                entity.eventType,
                entity.payload
            )

            // Map database entities to the ActivityQueryResult
            val queryResult = ActivityQueryResult(
                eventId = requireNotNull(entity.id),
                babyId = requireNotNull(entity.baby.id),
                babyFirstName = entity.baby.firstName,
                eventType = entity.eventType,
                eventTime = entity.eventTime,
                payload = parsedPayload,
                createdOn = entity.createdOn,
                modifiedOn = entity.modifiedOn
            )

            // Convert domain to response dto
            eventMapper.toActivityResponseDto(queryResult)
        }
    }
}
