package com.babytrackr.service.domain.model

import com.babytrackr.service.domain.enums.Sex
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Baby(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val nickname: String? = null,
    val birthDate: LocalDate,
    val sex: Sex? = null,
    val userId: String? = null,
    val createdOn: Instant,
    val modifiedOn: Instant
) {
   val ageWeeks: Long
       get() = ChronoUnit.DAYS.between(createdOn, modifiedOn)

   val ageMonths: Long
       get() = ChronoUnit.MONTHS.between(birthDate, LocalDate.now())

   val ageYears: Long
       get() = ChronoUnit.YEARS.between(birthDate, LocalDate.now())

   init {
      require(!birthDate.isAfter(LocalDate.now())) {
          "Birthdate cannot be in the future"
      }
   }
}