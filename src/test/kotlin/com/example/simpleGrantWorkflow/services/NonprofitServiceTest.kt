package com.example.simpleGrantWorkflow.services

import com.example.simpleGrantWorkflow.models.Nonprofit
import com.example.simpleGrantWorkflow.repositories.NonprofitRepository
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class NonprofitServiceTest {

    private lateinit var nonprofitRepository: NonprofitRepository
    private lateinit var nonprofitService: NonprofitService

    @BeforeEach
    fun setUp() {
        nonprofitRepository = mockk()
        nonprofitService = NonprofitService(nonprofitRepository)
    }

    @Test
    fun `getAllNonprofits should return a list of nonprofits`() {
        // Arrange
        val nonprofits = listOf(
            Nonprofit(name = "Helping Hands", "test@example.com", address = "123 Charity Lane"),
            Nonprofit(name = "Food for All", "test@example.com", address = "456 Food Street")
        )

        every { nonprofitRepository.findAll() } returns nonprofits

        // Act
        val result = nonprofitService.getAllNonprofits()

        // Assert
        assertEquals(2, result.size)
        assertEquals("Helping Hands", result[0].name)
        assertEquals("Food for All", result[1].name)
        verify(exactly = 1) { nonprofitRepository.findAll() }
    }

    @Test
    fun `getAllNonprofits should return an empty list when no nonprofits exist`() {
        // Arrange
        every { nonprofitRepository.findAll() } returns emptyList()

        // Act
        val result = nonprofitService.getAllNonprofits()

        // Assert
        assertTrue(result.isEmpty())
        verify(exactly = 1) { nonprofitRepository.findAll() }
    }

    @Test
    fun `createNonprofit should call save method and store nonprofit`() {
        // Arrange
        val nonprofit = Nonprofit(name = "Charity Org", "test@example.com", address = "789 Charity Road")

        every { nonprofitRepository.save(nonprofit) } just runs

        // Act
        nonprofitService.createNonprofit(nonprofit)

        // Assert
        verify(exactly = 1) { nonprofitRepository.save(nonprofit) }
    }

    @Test
    fun `findNonprofitByEmailAddress should return nonprofit when found`() {
        // Arrange
        val email = "contact@helpinghands.org"
        val nonprofit = Nonprofit(name = "Helping Hands", "contact@helpinghands.org", address = "123 Charity Lane")

        every { nonprofitRepository.findNonprofitByEmailAddress(email) } returns nonprofit

        // Act
        val result = nonprofitService.findNonprofitByEmailAddress(email)

        // Assert
        assertNotNull(result)
        assertEquals("Helping Hands", result?.name)
        assertEquals("123 Charity Lane", result?.address)
        verify(exactly = 1) { nonprofitRepository.findNonprofitByEmailAddress(email) }
    }

    @Test
    fun `findNonprofitByEmailAddress should return null when nonprofit is not found`() {
        // Arrange
        val email = "nonexistent@charity.org"

        every { nonprofitRepository.findNonprofitByEmailAddress(email) } returns null

        // Act
        val result = nonprofitService.findNonprofitByEmailAddress(email)

        // Assert
        assertNull(result)
        verify(exactly = 1) { nonprofitRepository.findNonprofitByEmailAddress(email) }
    }

    @Test
    fun `createNonprofit should throw an exception when repository fails`() {
        // Arrange
        val nonprofit = Nonprofit(name = "Helping Hands", "test@example.com", address = "123 Charity Lane")

        every { nonprofitRepository.save(nonprofit) } throws RuntimeException("Database error")

        // Act & Assert
        val exception = assertThrows<RuntimeException> {
            nonprofitService.createNonprofit(nonprofit)
        }

        assertEquals("Database error", exception.message)
        verify(exactly = 1) { nonprofitRepository.save(nonprofit) }
    }
}