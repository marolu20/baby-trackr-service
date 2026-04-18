package com.babytrackr.service.controller.model.response

import com.babytrackr.service.controller.model.request.Sex
import java.time.Instant
import java.time.LocalDate

data class BabyResponseDto(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val nickname: String? = null,
    val birthDate: LocalDate,
    val ageWeeks: Long,
    val ageMonths: Int,
    val sex: Sex,
    val ageYears: Long,
    val userId: String? = null,
    val createdOn: Instant,
    val modifiedOn: Instant
)

