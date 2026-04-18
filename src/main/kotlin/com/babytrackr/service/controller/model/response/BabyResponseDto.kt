package com.babytrackr.service.controller.model.response

import com.babytrackr.service.controller.model.request.Sex
import java.time.Instant

data class BabyResponseDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val nickname: String? = null,
    val ageMonths: Int? = null,
    val sex: Sex? = null,
    val userId: String? = null,
    val createdOn: Instant,
    val modifiedOn: Instant
)

