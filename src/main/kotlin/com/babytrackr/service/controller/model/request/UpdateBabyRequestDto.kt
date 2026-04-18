package com.babytrackr.service.controller.model.request

import java.time.LocalDate

data class UpdateBabyRequestDto(
    val firstName: String?,
    val lastName: String?,
    val nickname: String?,
    val birthDate: LocalDate,
    val sex: Sex?
)