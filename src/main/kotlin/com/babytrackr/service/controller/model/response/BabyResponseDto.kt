package com.babytrackr.service.controller.model.response

import com.babytrackr.service.domain.enums.Sex
import java.time.Instant
import java.time.LocalDate

data class BabyResponseDto(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val nickname: String? = null,
    val birthDate: LocalDate,
    val ageWeeks: Int,
    val ageMonths: Int,
    val sex: Sex,
    val ageYears: Int,
    val userId: String? = null,
    val createdOn: Instant,
    val modifiedOn: Instant
)

