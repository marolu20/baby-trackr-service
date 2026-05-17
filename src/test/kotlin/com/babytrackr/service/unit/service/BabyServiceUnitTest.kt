    package com.babytrackr.service.unit.service

    import com.babytrackr.service.application.services.BabyFinder
    import com.babytrackr.service.application.services.BabyService
    import com.babytrackr.service.domain.enums.Sex
    import com.babytrackr.service.exception.ErrorCode
    import com.babytrackr.service.exception.NotFoundException
    import com.babytrackr.service.fixtures.buildBabyEntity
    import com.babytrackr.service.fixtures.buildBabyRequestDto
    import com.babytrackr.service.fixtures.buildUpdateBabyRequestDto
    import com.babytrackr.service.infrastucture.repositories.BabyRepository
    import io.mockk.every
    import io.mockk.impl.annotations.InjectMockKs
    import io.mockk.impl.annotations.MockK
    import io.mockk.junit5.MockKExtension
    import io.mockk.just
    import io.mockk.runs
    import org.junit.jupiter.api.Assertions.assertEquals
    import org.junit.jupiter.api.extension.ExtendWith
    import io.mockk.verify
    import org.junit.jupiter.api.assertThrows
    import java.time.LocalDate
    import kotlin.test.Test

    @ExtendWith(MockKExtension::class)
    class BabyServiceUnitTest {

        @InjectMockKs
        lateinit var babyService: BabyService

        @MockK
        lateinit var babyRepository: BabyRepository

        @MockK
        lateinit var babyFinder: BabyFinder


        @Test
        fun `should create a baby and return response`() {

            // given
            val request = buildBabyRequestDto()
            val savedEntity = buildBabyEntity(1L)

            // stub the repository dependency
            every {
                babyRepository.save(any())
            } returns savedEntity

            // when
            val result = babyService.createBaby(request)

            // then
            assertEquals(1L, result.id)
            assertEquals("John", result.firstName)
            verify(exactly = 1) { babyRepository.save(any()) }
        }

        @Test
        fun `should get a baby and return response`() {

            // given
            val savedEntity = buildBabyEntity(1L)

            // stub the finder dependency
            every {
                babyFinder.getBabyOrThrow(any())
            } returns savedEntity

            // when
            val result = babyService.getBaby(1L)

            // then
            assertEquals(1L, result.id)
            assertEquals("John", result.firstName)
            verify(exactly = 1) { babyFinder.getBabyOrThrow(1L) }
        }

        @Test
        fun `should get a list of babies and return response`() {

            // given
            val savedEntity = buildBabyEntity(1L)

            // stub the repository dependency
            every {
                babyRepository.findAll()
            } returns listOf(savedEntity)

            // when
            val result = babyService.getAllBabies()

            // then
            assertEquals(1L, result.babies[0].id)
            assertEquals("John", result.babies[0].firstName)
            verify(exactly = 1) { babyRepository.findAll() }
        }

        @Test
        fun `should update a baby and return response`() {

            // given
            val request = buildUpdateBabyRequestDto(
                firstName = "Jason",
                lastName = "Smith",
                birthDate = LocalDate.now(),
                nickname = "JS",
                sex = Sex.MALE
            )

            val savedEntity = buildBabyEntity(1L)

            // stub the dependencies
            every {
                babyFinder.getBabyOrThrow(1L)
            } returns savedEntity

            every {
                babyRepository.save(any())
            } answers { firstArg() }

            // when
            val result = babyService.updateBaby(1L, request)

            // then
            assertEquals(1L, result.id)
            assertEquals("Jason", result.firstName)
            assertEquals("Smith", result.lastName)
            assertEquals("JS", result.nickname)
            verify(exactly = 1) { babyRepository.save(any()) }
            verify(exactly = 1) { babyFinder.getBabyOrThrow(1L) }
        }

        @Test
        fun `should delete a baby and not return response`() {

            // given
            val savedEntity = buildBabyEntity(1L)

            // stub the dependencies
            every { babyFinder.getBabyOrThrow(1L) } returns savedEntity

            every { babyRepository.deleteById(1L) } just runs

            // when
            babyService.deleteBaby(1L)

            // then
            verify(exactly = 1) { babyFinder.getBabyOrThrow(1L) }
            verify(exactly = 1) { babyRepository.deleteById(1L) }
        }

        @Test
        fun `should throw error if baby is not found`() {

            val babyId = 10L

            // given
            every { babyFinder.getBabyOrThrow(babyId) } throws NotFoundException(
                ErrorCode.BABY_NOT_FOUND,
                "Could not find baby=${babyId}"
            )

            // when
            val exception = assertThrows<NotFoundException> {
                babyService.getBaby(babyId)
            }

            // then
            assertEquals(ErrorCode.BABY_NOT_FOUND, exception.code)

            verify(exactly = 1) { babyFinder.getBabyOrThrow(babyId) }
        }

        @Test
        fun `should throw error if repository save fails`() {

            // given
            val request = buildBabyRequestDto()

            // stub the repository dependency
            every { babyRepository.save(any()) } throws RuntimeException("Database connection timeout")

            // when
            val exception = assertThrows<RuntimeException> {
                babyService.createBaby(request)
            }

            // then
            assertEquals("Database connection timeout", exception.message)
            verify(exactly = 1) { babyRepository.save(any()) }
        }

        @Test
        fun `should throw error if first name is missing`(){

            // given
            val request = buildBabyRequestDto(
                firstName = "",
                lastName = "Smith"
            )

            // when
            val exception = assertThrows<IllegalArgumentException> {
                babyService.createBaby(request)
            }

            // then
            assertEquals("First name cannot be blank", exception.message)

            verify(exactly = 0) { babyRepository.save(any()) }
        }

        @Test
        fun `should throw error if last name is missing`(){

            // given
            val request = buildBabyRequestDto(
                firstName = "Jason",
                lastName = ""
            )

            // when
            val exception = assertThrows<IllegalArgumentException> {
                babyService.createBaby(request)
            }

            // then
            assertEquals("Last name cannot be blank", exception.message)

            verify(exactly = 0) { babyRepository.save(any()) }
        }

        @Test
        fun `should throw error if birth date is in the future`(){

            // given
            val request = buildBabyRequestDto(
                firstName = "Jason",
                lastName = "Smith",
                birthDate = LocalDate.of(2999, 1, 1 )
            )

            // when
            val exception = assertThrows<IllegalArgumentException> {
                babyService.createBaby(request)
            }

            // then
            assertEquals("Birthdate cannot be in the future", exception.message)

            verify(exactly = 0) { babyRepository.save(any()) }
        }
    }