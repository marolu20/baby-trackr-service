package com.babytrackr.service.infrastucture.repositories

import com.babytrackr.service.domain.enums.EventType
import com.vladmihalcea.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Type
import java.time.Instant

@Entity
@Table(name = "events")
class EventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @Enumerated(EnumType.STRING)
    var eventType: EventType,
    @Type(JsonType::class)
    @Column(columnDefinition = "jsonb")
    var payload: String, // Map<String, Any>,
    var isCorrected: Boolean = false,
    @Type(JsonType::class)
    @Column(columnDefinition = "jsonb")
    var previousPayload: String? = null,
    var createdOn: Instant,
    var modifiedOn: Instant,
    @Column(name="version")
    var version: String = "v1",
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "baby_id")
    var baby: BabyEntity? = null,
)