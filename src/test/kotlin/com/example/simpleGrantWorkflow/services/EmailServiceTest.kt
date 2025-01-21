package com.example.simpleGrantWorkflow.services

import com.example.simpleGrantWorkflow.models.*
import com.example.simpleGrantWorkflow.repositories.EmailRepository
import com.example.simpleGrantWorkflow.utils.TemplateProcessor
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class EmailServiceTest {

    private lateinit var emailRepository: EmailRepository
    private lateinit var templateProcessor: TemplateProcessor
    private lateinit var nonprofitService: NonprofitService
    private lateinit var emailService: EmailService

    @BeforeEach
    fun setUp() {
        emailRepository = mockk()
        templateProcessor = mockk()
        nonprofitService = mockk()
        emailService = EmailService(emailRepository, templateProcessor)
    }

    @Test
    fun `sendEmail should return true when email is successfully saved`() {
        val email = Email(to = "to@example.com", from = "from@example.com", subject = "Subject", body = "Body")
        every { emailRepository.save(email) } returns true

        val result = emailService.sendEmail(email)

        assertTrue(result)
        verify { emailRepository.save(email) }
    }

    @Test
    fun `sendEmail should return false when email saving fails`() {
        val email = Email(to = "to@example.com", from = "from@example.com", subject = "Subject", body = "Body")
        every { emailRepository.save(email) } returns false

        val result = emailService.sendEmail(email)

        assertFalse(result)
        verify { emailRepository.save(email) }
    }

    @Test
    fun `findAllEmails should return a list of emails`() {
        val mockEmails = listOf(
            Email(to = "to1@example.com", from = "from@example.com", subject = "Subject1", body = "Body1"),
            Email(to = "to2@example.com", from = "from@example.com", subject = "Subject2", body = "Body2")
        )

        every { emailRepository.findAllEmails() } returns mockEmails

        val result = emailService.findAllEmails()

        assertEquals(2, result.size)
        assertEquals("to1@example.com", result[0].to)
        assertEquals("to2@example.com", result[1].to)
        verify { emailRepository.findAllEmails() }
    }

    @Test
    fun `processAndSendBulkEmails should throw exception when recipient list is empty`() {
        val request = BulkEmailRequest(from = "from@example.com", subject = "Subject", body = "Body", to = emptyList())

        val exception = assertThrows<IllegalArgumentException> {
            emailService.processAndSendBulkEmails(request, nonprofitService)
        }

        assertEquals("Recipient list cannot be empty", exception.message)
    }

    @Test
    fun `processAndSendBulkEmails should skip emails with no associated nonprofit`() {
        val request = BulkEmailRequest(
            from = "from@example.com",
            subject = "Subject",
            body = "Body",
            to = listOf("valid@example.com", "invalid@example.com")
        )

        every { nonprofitService.findNonprofitByEmailAddress("valid@example.com") } returns Nonprofit("Valid Nonprofit", "valid@example.com", "123 Street")
        every { nonprofitService.findNonprofitByEmailAddress("invalid@example.com") } returns null
        every { emailRepository.save(any()) } returns true
        every { templateProcessor.processTemplate(any(), any()) } returns "Processed Body"

        val result = emailService.processAndSendBulkEmails(request, nonprofitService)

        assertEquals(1, result.successCount)
        assertEquals(0, result.failureCount)
        assertEquals(1, result.skippedEmails.size)
        assertEquals("invalid@example.com", result.skippedEmails.first())
    }

    @Test
    fun `processAndSendBulkEmails should return failure for emails that cannot be sent`() {
        val request = BulkEmailRequest(
            from = "from@example.com",
            subject = "Subject",
            body = "Body",
            to = listOf("fail@example.com", "success@example.com")
        )

        val nonprofit = Nonprofit("Test Nonprofit", "fail@example.com", "123 Address")
        every { nonprofitService.findNonprofitByEmailAddress(any()) } returns nonprofit
        every { templateProcessor.processTemplate(any(), any()) } returns "Processed Body"
        every { emailRepository.save(match { it.to == "fail@example.com" }) } returns false
        every { emailRepository.save(match { it.to == "success@example.com" }) } returns true

        val result = emailService.processAndSendBulkEmails(request, nonprofitService)

        assertEquals(1, result.successCount)
        assertEquals(1, result.failureCount)
        assertEquals("Failed to send email to fail@example.com", result.failedEmails.first())
    }

    @Test
    fun `processEmailBody should replace placeholders correctly`() {
        val nonprofit = Nonprofit("Helping Hands", "test@example.com", "123 Charity Lane")
        every { templateProcessor.processTemplate(any(), any()) } returns "Hello Helping Hands, your address is 123 Charity Lane."

        val body = emailService.processAndSendBulkEmails(
            BulkEmailRequest(
                from = "from@example.com",
                subject = "Test Subject",
                body = "Hello {{name}}, your address is {{address}}.",
                to = listOf("test@example.com")
            ),
            nonprofitService
        )

        assertEquals(1, body.successCount)
        verify { templateProcessor.processTemplate("Hello {{name}}, your address is {{address}}.", mapOf("name" to "Helping Hands", "address" to "123 Charity Lane")) }
    }
}