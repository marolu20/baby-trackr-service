package com.babytrackr.service.infrastucture.repositories

import com.babytrackr.service.domain.enums.Sex
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(name = "babies")
class BabyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var firstName: String,
    var lastName: String,
    var nickname: String? = null,
    var birthDate: LocalDate? = null,
    var sex: Sex? = null,
    var userId: String? = null,
    var createdOn: Instant,
    var modifiedOn: Instant,
    @OneToMany(mappedBy = "baby", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var events: MutableList<EventEntity> = mutableListOf()
)