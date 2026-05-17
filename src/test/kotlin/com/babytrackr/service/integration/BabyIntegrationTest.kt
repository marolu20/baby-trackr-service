package com.babytrackr.service.integration

import com.babytrackr.service.fixtures.buildBabyRequestDto
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.junit.jupiter.api.Test

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class BabyIntegrationTest() {

    @Autowired
    lateinit var mockMvc: MockMvc


    private val objectMapper = jacksonObjectMapper().findAndRegisterModules()

    @Test
    fun `should create baby and persist in database`() {

        // given
        val request = buildBabyRequestDto()

        // when
        mockMvc.post("/v1/babies") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            // then
            .andExpect {
                status { isCreated() }
                jsonPath("$.id") { exists() }
                jsonPath("$.firstName") { value("John") }
                jsonPath("$.lastName") { value("Doe") }
            }
    }
}
