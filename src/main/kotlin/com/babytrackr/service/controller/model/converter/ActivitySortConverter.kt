package com.babytrackr.service.controller.model.converter

import com.babytrackr.service.controller.model.request.ActivitySort
import com.babytrackr.service.controller.model.request.ActivitySortDirection
import com.babytrackr.service.controller.model.request.ActivitySortField
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class ActivitySortConverter: Converter<String, ActivitySort> {
    override fun convert(source:String): ActivitySort {
        val parts = source.split(",")

        if (parts.size != 2) {
            throw IllegalArgumentException(
                "sortBy must be in the format field,direction"
            )
        }

        val field = when (parts[0].lowercase()) {
            "timestamp" -> ActivitySortField.TIMESTAMP
            "type" -> ActivitySortField.TYPE
            else -> throw IllegalArgumentException(
                "Invalid sort field: ${parts[0]}"
            )
        }

        val direction = when (parts[1].lowercase()) {
            "ascending" -> ActivitySortDirection.ASCENDING
            "descending" -> ActivitySortDirection.DESCENDING
            else -> throw IllegalArgumentException(
                "Invalid sort direction: ${parts[1]}"
            )
        }

        return ActivitySort(
            field = field,
            direction = direction
        )
    }
}
