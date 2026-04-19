package com.babytrackr.service.controller.model

import com.babytrackr.service.controller.model.request.CreateBabyRequestDto
import com.babytrackr.service.controller.model.request.UpdateBabyRequestDto
import com.babytrackr.service.controller.model.response.BabyResponseDto
import com.babytrackr.service.controller.model.response.GetAllBabyResponseDto
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
@RequestMapping("/v1/babies")
class BabyController {

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun createBaby(
        @RequestBody @Valid request: CreateBabyRequestDto
    ): BabyResponseDto {
        return // call service
    }

    @GetMapping("/{babyId}")
    @ResponseStatus(HttpStatus.OK)
    fun retrieveBaby(@PathVariable("babyId") babyId: Long): BabyResponseDto {
        return // call service
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    fun retrieveAllBabies(): GetAllBabyResponseDto {
        return // call service
    }

    @PatchMapping("/{babyId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateBaby(
        @PathVariable("babyId") babyId: Long,
        @RequestBody @Valid request: UpdateBabyRequestDto
        ): BabyResponseDto {
        return // call service
    }

    @DeleteMapping("/{babyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBaby(@PathVariable("babyId") babyId: Long) {
    }
}