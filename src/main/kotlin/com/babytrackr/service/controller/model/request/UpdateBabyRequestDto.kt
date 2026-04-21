package com.babytrackr.service.controller.model.request

import com.babytrackr.service.domain.enums.Sex
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = false)
data class UpdateBabyRequestDto(
    val firstName: String?,
    val lastName: String?,
    val nickname: String?,
    val birthDate: LocalDate?,
    val sex: Sex?
)