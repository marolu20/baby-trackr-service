package com.babytrackr.service.unit.controller

import com.babytrackr.service.application.services.BabyService
import com.babytrackr.service.controller.model.BabyController
import com.babytrackr.service.controller.model.response.GetAllBabyResponseDto
import com.babytrackr.service.domain.enums.Sex
import com.babytrackr.service.exception.ErrorCode
import com.babytrackr.service.exception.NotFoundException
import com.babytrackr.service.fixtures.buildBabyRequestDto
import com.babytrackr.service.fixtures.buildBabyResponseDto
import com.babytrackr.service.fixtures.buildGetAllBabiesResponseDto
import com.babytrackr.service.fixtures.buildUpdateBabyRequestDto
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.junit.jupiter.api.Test
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.verify
import java.time.LocalDate

@WebMvcTest(BabyController::class)
class BabyControllerUnitTest(
    @Autowired
    private val mockMvc: MockMvc
) {

    private val objectMapper = jacksonObjectMapper().findAndRegisterModules()

    @MockkBean
    lateinit var babyServiceMock: BabyService

    @Test
    fun `should create a new baby`(){

        // given
        val request = buildBabyRequestDto()

        val response = buildBabyResponseDto(1)

        every {
            babyServiceMock.createBaby(any())
        } returns response

        // when
        mockMvc.post("/v1/babies") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }

        // then
            .andExpect {
                status { isCreated() }
                jsonPath("$.id") { value(1) }
                jsonPath("$.firstName") { value("John") }
            }
    }

    @Test
    fun `should get a baby`() {

        // given
        val id = 1L

        val response = buildBabyResponseDto(id)

        every {
            babyServiceMock.getBaby(id)
        } returns response

        // when
        mockMvc.get("/v1/babies/{babyId}", id) {
            accept = MediaType.APPLICATION_JSON
        }
            // then
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(id) }

            }
    }

    @Test
    fun `should get a list of babies`() {

        // given
        val id = 1L

        val response = buildGetAllBabiesResponseDto(id)

        every {
            babyServiceMock.getAllBabies()
        } returns response

        // when
        mockMvc.get("/v1/babies") {
            accept = MediaType.APPLICATION_JSON
        }
            // then
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.babies.[0].id") { value(id) }
            }
    }

    @Test
    fun `should update a baby`() {

        // given
        val id = 1L
        val request = buildUpdateBabyRequestDto(
            firstName = "Jason",
            lastName = "Smith",
            birthDate = LocalDate.now(),
            nickname = "JS",
            sex = Sex.MALE
        )

        val response = buildBabyResponseDto(
            id,
            firstName = "Jason",
            lastName = "Smith",
            nickname = "JS"
        )

        every {
            babyServiceMock.updateBaby(eq(id), request)
        } returns response

        // when
        mockMvc.patch("/v1/babies/{babyId}", id) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
            accept = MediaType.APPLICATION_JSON
        }
            // then
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(id) }
                jsonPath("$.firstName") { value("Jason") }
                jsonPath("$.lastName") { value("Smith") }
                jsonPath("$.nickname") { value("JS") }
            }
    }

    @Test
        fun `should delete a baby`() {

            // given
            val id = 1L
        
            every {
                babyServiceMock.deleteBaby(id)
            } just runs

            // when
            mockMvc.delete("/v1/babies/{babyId}", id) {
                accept = MediaType.APPLICATION_JSON
            }
                // then
                .andExpect {
                    status { isNoContent() }
                }
        }

    @Test
    fun `should return 404 if baby not found when retrieving a baby`() {
        val babyId = 299L

        every {
            babyServiceMock.getBaby(babyId)
        } throws NotFoundException(
            ErrorCode.BABY_NOT_FOUND,"Could not find baby=${babyId}"
        )

        // when
        mockMvc.get("/v1/babies/{babyId}", babyId) {}
            // then
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun `should return 400 when baby first name is blank when creating a new baby`() {

        val request = buildBabyRequestDto(firstName = "")

        // when
        mockMvc.post("/v1/babies") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            // then
            .andExpect {
                status { isBadRequest() }
            }
    }

    @Test
    fun `should return empty list if no babies are found`(){
        // given
        every {
            babyServiceMock.getAllBabies()
        } returns GetAllBabyResponseDto(emptyList())

        //when
        mockMvc.get("/v1/babies") {
            accept = MediaType.APPLICATION_JSON
        }
            // then
            .andExpect {
                jsonPath("$.babies.length()") { value((0)) }
            }
    }

    @Test
    fun `should call baby service`() {

        val babyId = 10L

        // given
        val createRequest = buildBabyRequestDto()
        val updateRequest = buildUpdateBabyRequestDto(
            firstName = "Jason",
            lastName = "Smith",
            birthDate = LocalDate.now(),
            nickname = "JS",
            sex = Sex.MALE
        )
        val getResponse = buildBabyResponseDto(babyId)

        val getAllResponse = buildGetAllBabiesResponseDto(babyId)

        every {
            babyServiceMock.createBaby(any())
        } returns getResponse

        every {
            babyServiceMock.getBaby(babyId)
        } returns getResponse

        every {
            babyServiceMock.getAllBabies()
        } returns getAllResponse

        every {
            babyServiceMock.updateBaby(babyId, any())
        } returns getResponse

        every {
            babyServiceMock.deleteBaby(babyId)
        } just runs

        // when
        mockMvc.post("/v1/babies") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createRequest)
            accept = MediaType.APPLICATION_JSON
        }

        mockMvc.get("/v1/babies/{babyId}", babyId) {
            accept = MediaType.APPLICATION_JSON
        }

        mockMvc.get("/v1/babies") {
            accept = MediaType.APPLICATION_JSON
        }

        mockMvc.patch("/v1/babies/{babyId}", babyId, updateRequest) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updateRequest)
            accept = MediaType.APPLICATION_JSON
        }

        mockMvc.delete("/v1/babies/{babyId}", babyId) {
            accept = MediaType.APPLICATION_JSON
        }

        // then
        verify(exactly = 1){ babyServiceMock.createBaby(any()) }
        verify(exactly = 1){ babyServiceMock.getBaby(babyId) }
        verify(exactly = 1){ babyServiceMock.getAllBabies() }
        verify(exactly = 1){ babyServiceMock.updateBaby(babyId, any()) }
        verify(exactly = 1) {babyServiceMock.deleteBaby(babyId)}
    }
}
