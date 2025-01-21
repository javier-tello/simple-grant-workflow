package com.example.simpleGrantWorkflow.services

import com.example.simpleGrantWorkflow.models.BulkEmailRequest
import com.example.simpleGrantWorkflow.models.BulkEmailResponse
import com.example.simpleGrantWorkflow.models.Email
import com.example.simpleGrantWorkflow.models.Nonprofit
import com.example.simpleGrantWorkflow.repositories.EmailRepository
import com.example.simpleGrantWorkflow.utils.TemplateProcessor

class EmailService(private val emailRepository: EmailRepository,  private val templateProcessor: TemplateProcessor) {
    fun sendEmail(email: Email): Boolean = emailRepository.save(email)

    fun findAllEmails(): List<Email> = emailRepository.findAllEmails()

    fun processAndSendBulkEmails(bulkEmailRequest: BulkEmailRequest, nonprofitService: NonprofitService): BulkEmailResponse {
        require(bulkEmailRequest.to.isNotEmpty()) { "Recipient list cannot be empty" }

        val skippedEmails = mutableListOf<String>()
        val (successfulEmails, failedEmails) = bulkEmailRequest.to.mapNotNull { emailAddress ->
            val nonprofit = nonprofitService.findNonprofitByEmailAddress(emailAddress)
            if (nonprofit == null) {
                skippedEmails.add(emailAddress)
                null
            } else {
                val email = buildEmail(bulkEmailRequest, nonprofit, emailAddress)
                emailAddress to sendEmail(email)
            }
        }.partition { it.second }

        return BulkEmailResponse(
            successCount = successfulEmails.size,
            failureCount = failedEmails.size,
            failedEmails = failedEmails.map { "Failed to send email to ${it.first}" },
            message = if (failedEmails.isEmpty()) "All emails sent successfully" else "Some emails failed",
            skippedEmails = skippedEmails
        )
    }

    private fun processEmailBody(bodyTemplate: String, nonprofit: Nonprofit): String {
        val placeholders = mapOf(
            "name" to nonprofit.name,
            "address" to nonprofit.address
        )
        return templateProcessor.processTemplate(bodyTemplate, placeholders)
    }

    private fun buildEmail(bulkEmailRequest: BulkEmailRequest, nonprofit: Nonprofit, emailAddress: String): Email {
        val processedBody = processEmailBody(bulkEmailRequest.body, nonprofit)
        return Email(to = emailAddress, from = bulkEmailRequest.from, subject = bulkEmailRequest.subject, body = processedBody)
    }
}