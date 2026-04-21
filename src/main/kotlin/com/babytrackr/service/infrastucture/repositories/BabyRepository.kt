package com.babytrackr.service.infrastucture.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BabyRepository: CrudRepository<BabyEntity, Long> {}

