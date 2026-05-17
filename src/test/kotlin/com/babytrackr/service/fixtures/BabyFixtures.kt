package com.babytrackr.service.fixtures

import com.babytrackr.service.controller.model.request.CreateBabyRequestDto
import com.babytrackr.service.controller.model.request.UpdateBabyRequestDto
import com.babytrackr.service.controller.model.response.BabyResponseDto
import com.babytrackr.service.controller.model.response.GetAllBabyResponseDto
import com.babytrackr.service.domain.enums.Sex
import com.babytrackr.service.infrastucture.repositories.BabyEntity
import java.time.LocalDate
import java.time.Instant

fun buildBabyRequestDto(
    firstName: String = "John",
    lastName: String = "Doe",
    birthDate: LocalDate = LocalDate.of(1970, 1, 1 )
): CreateBabyRequestDto {
    return CreateBabyRequestDto(
        firstName = firstName,
        lastName = lastName,
        nickname = "JD",
        birthDate = birthDate,
        sex = Sex.MALE,
        userId = "1"
    )
}

fun buildBabyResponseDto(
    id: Long,
    firstName: String = "John",
    lastName: String = "Doe",
    nickname: String = "JD",
): BabyResponseDto {
    return BabyResponseDto(
        id = id,
        firstName = firstName,
        lastName = lastName,
        nickname = nickname,
        birthDate = LocalDate.of(1970, 1, 1),
        ageWeeks = 10,
        ageMonths = 1,
        sex = Sex.MALE,
        ageYears = 0,
        userId = "1",
        createdOn = Instant.now(),
        modifiedOn = Instant.now()
    )
}

fun buildGetAllBabiesResponseDto(
    id: Long
): GetAllBabyResponseDto {
    val babies = buildBabyResponseDto(id)
    return GetAllBabyResponseDto(listOf(babies))
}

fun buildUpdateBabyRequestDto(
    firstName: String?,
    lastName: String?,
    birthDate: LocalDate?,
    nickname: String?,
    sex: Sex?
): UpdateBabyRequestDto {
    return UpdateBabyRequestDto(
        firstName = firstName ?: "John",
        lastName = lastName ?: "Doe",
        nickname = nickname ?: "JD",
        birthDate = birthDate ?: LocalDate.of(2026, 5, 16),
        sex = sex ?: Sex.MALE,
    )
}

fun buildBabyEntity(
    id: Long? = null,
    firstName: String = "John",
    lastName: String = "Doe",
    nickname: String? = null,
    birthDate: LocalDate = LocalDate.of(2025, 5, 16),
    sex: Sex? = Sex.MALE,
    userId: String? = null,
    createdOn: Instant = Instant.now(),
    modifiedOn: Instant = Instant.now()
): BabyEntity {
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