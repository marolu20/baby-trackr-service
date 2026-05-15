package com.babytrackr.service.fixtures

import com.babytrackr.service.controller.model.request.CreateBabyRequestDto
import com.babytrackr.service.controller.model.request.UpdateBabyRequestDto
import com.babytrackr.service.controller.model.response.BabyResponseDto
import com.babytrackr.service.controller.model.response.GetAllBabyResponseDto
import com.babytrackr.service.domain.enums.Sex
import java.time.LocalDate
import java.time.Instant

fun buildBabyRequestDto(
    firstName: String = "John",
    lastName: String = "Doe"
): CreateBabyRequestDto {
    return CreateBabyRequestDto(
        firstName = firstName,
        lastName = lastName,
        nickname = "JD",
        birthDate = LocalDate.of(1970, 1, 1),
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