package com.babytrackr.service.controller.model.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

enum class Sex {
    MALE,
    FEMALE
}

data class CreateBabyRequestDto(
    @field:NotBlank("first name required")
    val firstName: String,
    @field:NotBlank("last name required")
    val lastName: String,
    val nickname: String? = null,
    @field:Min(0, message="age must be greater than or equal to 0")
    val birthDate: LocalDate,
    val sex: Sex? = null,
    val userId: String? = null
)
