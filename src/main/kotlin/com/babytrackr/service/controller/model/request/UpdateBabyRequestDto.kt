package com.babytrackr.service.controller.model.request

import com.babytrackr.service.domain.enums.Sex
import java.time.LocalDate

data class UpdateBabyRequestDto(
    val firstName: String?,
    val lastName: String?,
    val nickname: String?,
    val birthDate: LocalDate,
    val sex: Sex?
)