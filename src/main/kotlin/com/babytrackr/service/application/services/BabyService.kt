package com.babytrackr.service.application.services

import com.babytrackr.service.application.util.toTitleCase
import com.babytrackr.service.controller.model.request.CreateBabyRequestDto
import com.babytrackr.service.controller.model.response.BabyResponseDto
import com.babytrackr.service.domain.model.Baby
import com.babytrackr.service.infrastucture.repositories.BabyRepository
import org.springframework.stereotype.Service
import com.babytrackr.service.application.mapper.toEntity
import com.babytrackr.service.application.mapper.toDomain
import com.babytrackr.service.application.mapper.toBabyResponseDto
import com.babytrackr.service.controller.model.request.UpdateBabyRequestDto
import com.babytrackr.service.controller.model.response.GetAllBabyResponseDto
import java.time.Instant

@Service
class BabyService(
    private val babyRepository: BabyRepository,
    private val babyFinder: BabyFinder
) {

    fun createBaby(request: CreateBabyRequestDto): BabyResponseDto {
        val currentDate = Instant.now()

        val baby = Baby(
            id = null,
            firstName =  toTitleCase(request.firstName),
            lastName =  toTitleCase(request.lastName),
            nickname = request.nickname,
            birthDate = request.birthDate,
            sex = request.sex,
            userId = request.userId,
            createdOn = currentDate,
            modifiedOn = currentDate
        )

        val babyEntity = baby.toEntity()

        val persistedBaby = babyRepository.save(babyEntity)

        val savedDomain = persistedBaby.toDomain()

        return savedDomain.toBabyResponseDto()
    }


    fun getBaby(id: Long): BabyResponseDto {
        val persistedBaby = babyFinder.getBabyOrThrow(id)

        val savedDomain = persistedBaby.toDomain()

        return savedDomain.toBabyResponseDto()

    }

    fun getAllBabies(): GetAllBabyResponseDto {
        val persistedBabies = babyRepository.findAll()
            .map { it.toDomain() }

        return GetAllBabyResponseDto(persistedBabies.map { it.toBabyResponseDto() })

    }

    fun updateBaby(id: Long, request: UpdateBabyRequestDto): BabyResponseDto {

        val persistedBaby = babyFinder.getBabyOrThrow(id)

        val baby = persistedBaby.toDomain()

        val updatedBaby = baby.update(
            firstName = request.firstName,
            lastName = request.lastName,
            nickname = request.nickname,
            birthDate = request.birthDate,
            sex = request.sex,
        )

        val babyEntity = updatedBaby.toEntity()

        val savedEntity = babyRepository.save(babyEntity)

        val savedDomain = savedEntity.toDomain()
        return savedDomain.toBabyResponseDto()
    }

    fun deleteBaby(id: Long) {
        babyFinder.getBabyOrThrow(id)

        babyRepository.deleteById(id)
    }
}