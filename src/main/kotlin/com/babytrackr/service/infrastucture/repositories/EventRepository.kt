package com.babytrackr.service.infrastucture.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository: CrudRepository<EventEntity, Long> {
    fun findByBabyId(babyId: Long): List<EventEntity>
    fun findByIdAndBabyId(id: Long, babyId: Long): EventEntity?
}
