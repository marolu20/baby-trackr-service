package com.babytrackr.service.domain.model

import com.babytrackr.service.controller.model.request.Sex
import java.time.Instant

data class Baby(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val nickname: String? = null,
    val ageMonths: Int? = null,
    val sex: Sex? = null,
    val userId: String? = null,
    val createdOn: Instant,
    val modifiedOn: Instant
) {
   init {
       if (ageMonths != null) {
           require(ageMonths >= 0) { "ageMonths must be >= 0" }
       }
   }
}