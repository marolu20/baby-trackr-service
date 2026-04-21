package com.babytrackr.service.application.services

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
import kotlin.String

@Service
class BabyService(
    private val babyRepository: BabyRepository
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

        // convert domain to entity
        val entity = baby.toEntity()

        // save to repo
        val savedEntity = babyRepository.save(entity)

        // convert entity to Domain
        val savedDomain = savedEntity.toDomain()

        //  convert to ResponseDto
        return savedDomain.toBabyResponseDto()
    }


    fun getBaby(id: Long): BabyResponseDto {
        val babyEntity = babyRepository.findById(id)
            .orElseThrow { IllegalStateException("Could not find baby") }

        val savedDomain = babyEntity.toDomain()

        return savedDomain.toBabyResponseDto()

    }

    fun getAllBabies(): GetAllBabyResponseDto {
        val babyEntities = babyRepository.findAll()
            .map { it.toDomain() }

        return GetAllBabyResponseDto(babyEntities.map { it.toBabyResponseDto() })

    }

    fun updateBaby(id: Long, request: UpdateBabyRequestDto): BabyResponseDto {

        // check if id exists in the database
        val babyEntity = babyRepository.findById(id)
            .orElseThrow{ IllegalStateException("Could not find baby") }

        request.firstName?.let { babyEntity.firstName = it }
        request.lastName?.let { babyEntity.lastName = it }
        request.nickname?.let { babyEntity.nickname = it }
        request.birthDate?.let { babyEntity.birthDate = it }
        request.sex?.let { babyEntity.sex = it }

        babyEntity.modifiedOn = Instant.now()

        val savedEntity = babyRepository.save(babyEntity)

        val savedDomain = savedEntity.toDomain()
        return savedDomain.toBabyResponseDto()
    }

    fun deleteBaby(id: Long) {
        babyRepository.findById(id)
            .orElseThrow{ IllegalStateException("Could not find baby") }

        babyRepository.deleteById(id)
    }
    /*
    Capitalizes the first character of a string
     */
    private fun toTitleCase(name: String) = name.replaceFirstChar { it.titlecase() }

}