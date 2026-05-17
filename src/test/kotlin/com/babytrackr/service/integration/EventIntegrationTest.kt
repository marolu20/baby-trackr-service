package com.babytrackr.service.integration

import com.babytrackr.service.application.services.BabyService
import com.babytrackr.service.fixtures.buildBabyRequestDto
import com.babytrackr.service.fixtures.buildEventRequestDto
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jayway.jsonpath.JsonPath
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class EventIntegrationTest(
    @Autowired private val mockMvc: MockMvc
) {

    private val objectMapper = jacksonObjectMapper().findAndRegisterModules()

    @Test
    fun `should create event and persist in database`() {

        // create a new baby using the controller api
        val babyRequest = buildBabyRequestDto()

        val babyResponseJson =
            mockMvc.post("/v1/babies") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(babyRequest)
            }.andExpect {
                status { isCreated() }
            }.andReturn().response.contentAsString

        // extract the baby id (cast as Int) from the HTTP response
        val babyId = JsonPath.read<Int>(babyResponseJson, "$.id")

        // given
        val request = buildEventRequestDto()

        // when
        mockMvc.post("/v1/babies/{babyId}/events",babyId) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            // then
            .andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { exists() }
                jsonPath("$.babyId") { value(babyId) }
                jsonPath("$.eventType") { value("FEED") }
                jsonPath("$.payload.feedingAmount") { value(5) }
            }
    }
}
