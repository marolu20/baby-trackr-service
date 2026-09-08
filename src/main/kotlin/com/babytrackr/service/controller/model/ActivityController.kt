package com.babytrackr.service.controller.model

import com.babytrackr.service.application.services.ActivityService
import com.babytrackr.service.controller.model.request.ActivityFilter
import com.babytrackr.service.controller.model.request.ActivityPagination
import com.babytrackr.service.controller.model.response.ActivityResponse
import com.babytrackr.service.controller.model.response.PaginationResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
@RequestMapping("/v1/activity")
class ActivityController(private val activityService: ActivityService) {

    @GetMapping()
    fun getActivity(
        filter: ActivityFilter,
        pagination: ActivityPagination
    ): PaginationResponse<ActivityResponse> {
        val activityPage = activityService.getActivityReport(filter, pagination)

        return PaginationResponse(
            data = activityPage.content,
            page = activityPage.number + 1,
            pageSize = activityPage.size,
            totalPages = activityPage.totalPages,
            totalItems = activityPage.totalElements
        )
    }
}
