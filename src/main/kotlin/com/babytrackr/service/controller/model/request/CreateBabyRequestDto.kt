package com.babytrackr.service.controller.model.request

import com.babytrackr.service.domain.enums.Sex
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class CreateBabyRequestDto(
    @field:NotBlank("first name required")
    val firstName: String,
    @field:NotBlank("last name required")
    val lastName: String,
    val nickname: String? = null,
    val birthDate: LocalDate,
    val sex: Sex? = null,
    val userId: String? = null
)
