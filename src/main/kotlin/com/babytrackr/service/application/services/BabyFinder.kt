package com.babytrackr.service.application.services

import com.babytrackr.service.exception.ErrorCode
import com.babytrackr.service.exception.NotFoundException
import com.babytrackr.service.infrastucture.repositories.BabyEntity
import com.babytrackr.service.infrastucture.repositories.BabyRepository
import org.springframework.stereotype.Component

@Component
class BabyFinder(
    private val babyRepository: BabyRepository,
) {
    fun getBabyOrThrow(babyId: Long): BabyEntity { // should this live in both services?
        return babyRepository.findById(babyId)
            .orElseThrow {
                throw NotFoundException(
                    ErrorCode.BABY_NOT_FOUND,
                    "Could not find baby=${babyId}"
                )
            }
    }
}