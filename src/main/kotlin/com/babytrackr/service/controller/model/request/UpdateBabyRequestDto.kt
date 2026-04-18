package com.babytrackr.service.controller.model.request

data class UpdateBabyRequestDto(
    val firstName: String?,
    val lastName: String?,
    val nickname: String?,
    val ageMonths: Int?,
    val sex: Sex?
)