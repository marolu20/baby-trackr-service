package com.babytrackr.service.controller.model.request

import com.babytrackr.service.domain.enums.EventType
import java.time.LocalDate

data class ActivityFilter(
    val babyId: Long? = null,
    val type: EventType? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
)

data class ActivityPagination(
    val page: Int? = null,
    val pageSize: Int? = null,
    val sortBy: ActivitySort = ActivitySort()
)

data class ActivitySort(
    val field: ActivitySortField = ActivitySortField.TIMESTAMP,
    val direction: ActivitySortDirection = ActivitySortDirection.DESCENDING
)

enum class ActivitySortField {
    TIMESTAMP,
    TYPE
}

enum class ActivitySortDirection {
    ASCENDING,
    DESCENDING
}
