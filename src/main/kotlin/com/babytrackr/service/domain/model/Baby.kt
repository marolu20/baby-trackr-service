package com.babytrackr.service.domain.model

import com.babytrackr.service.application.util.toTitleCase
import com.babytrackr.service.domain.enums.Sex
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Baby(
    val id: Long?,
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

    fun update(
        firstName: String?,
        lastName: String?,
        nickname: String?,
        birthDate: LocalDate?,
        sex:Sex?
    ): Baby {
        val updated = this.copy(
            firstName = firstName?.let { toTitleCase(it) } ?: this.firstName,
            lastName = lastName?.let { toTitleCase(it) } ?: this.lastName,
            nickname = nickname ?: this.nickname,
            birthDate = birthDate ?: this.birthDate,
            sex = sex ?: this.sex,
            modifiedOn = Instant.now()
        )

        validate(updated)

        return updated
    }

    /*
    Validates the first and last name to ensure the user does not pass in blank fields
    Also validates the birthdate to ensure the user does not pass in a date that is in the future.
     */
    fun validate(baby: Baby) {
        require(baby.firstName.isNotBlank()) { "First name cannot be blank" }
        require(baby.lastName.isNotBlank()) { "Last name cannot be blank" }
        require(!baby.birthDate.isAfter(LocalDate.now())) { "Birthdate cannot be in the future" }
    }
}