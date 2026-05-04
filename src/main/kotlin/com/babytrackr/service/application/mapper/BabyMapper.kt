package com.babytrackr.service.application.mapper

import com.babytrackr.service.controller.model.response.BabyResponseDto
import com.babytrackr.service.domain.model.Baby
import com.babytrackr.service.infrastucture.repositories.BabyEntity

//Domain to Entity
fun Baby.toEntity(): BabyEntity {
    return BabyEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        nickname = nickname,
        birthDate = birthDate,
        sex = sex,
        userId = userId,
        createdOn = createdOn,
        modifiedOn = modifiedOn
    )
}

// Entity to Domain
fun BabyEntity.toDomain(): Baby {
    return Baby(
        id = id,
        firstName = firstName,
        lastName = lastName,
        nickname = nickname,
        birthDate = birthDate ?: throw IllegalStateException("birthDate cannot be null"),
        sex = sex,
        userId = userId,
        createdOn = modifiedOn,
        modifiedOn = modifiedOn
    )
}

//Domain to Response DTO
fun Baby.toBabyResponseDto(): BabyResponseDto {
    return BabyResponseDto(
        id = id ?: throw IllegalStateException("id cannot be null"),
        firstName = firstName,
        lastName = lastName,
        nickname = nickname,
        birthDate = birthDate,
        ageWeeks = ageWeeks.toInt(),
        ageMonths = ageMonths.toInt(),
        ageYears = ageYears.toInt(),
        sex = sex ?: throw IllegalStateException("sex cannot be null"),
        userId = userId,
        createdOn = createdOn,
        modifiedOn = modifiedOn
    )
}