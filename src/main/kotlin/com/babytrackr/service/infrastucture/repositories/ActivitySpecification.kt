package com.babytrackr.service.infrastucture.repositories

import com.babytrackr.service.controller.model.request.ActivityFilter
import com.babytrackr.service.domain.enums.EventType
import org.springframework.data.jpa.domain.Specification
import java.time.ZoneOffset

class ActivitySpecification(
    private val filter: ActivityFilter
) {

    fun hasBabyId(): Specification<EventEntity>{
        return Specification { root, _, cb ->
            cb.equal(
                root.get<BabyEntity>("baby").get<Long>("id"),
                filter.babyId
            )
        }
    }

    fun hasType(): Specification<EventEntity>{
        return Specification { root, _, cb ->
            cb.equal(root.get<EventType>("eventType"), filter.type)
        }
    }

    fun hasDates(): Specification<EventEntity> {

        return Specification { root, _, cb ->
            val start = requireNotNull(filter.startDate)
            val end = requireNotNull(filter.endDate)

            val zoneId = ZoneOffset.UTC

            // normalize LocalDate to lower and uppper Instant boundaries in UTC
            val startInstant = start.atStartOfDay(zoneId).toInstant() // Function invocation 'zoneId()' expected., Argument type mismatch: actual type is 'TemporalQuery<ZoneId!>!', but 'ZoneId!' was expected.
            val endInstant = end.plusDays(1)
                .atStartOfDay(zoneId)
                .toInstant()

            cb.and(
                cb.greaterThanOrEqualTo(root.get("eventTime"), startInstant),
                cb.lessThan(root.get("eventTime"), endInstant)
            )
        }
    }

    fun build(): Specification<EventEntity> {
        val specs = mutableListOf<Specification<EventEntity>>()

        if (filter.babyId != null) {
            specs.add(hasBabyId())
        }

        if (filter.type != null) {
            specs.add(hasType())
        }

        if (filter.startDate != null && filter.endDate != null) {
            specs.add(hasDates())
        }

        if (specs.isEmpty()) {
            return Specification{ _, _, cb -> cb.conjunction() }
        }

        var result = specs[0]

        for (i in 1 until specs.size) {
            result = result.and(specs[i])
        }
        return result
    }
}
