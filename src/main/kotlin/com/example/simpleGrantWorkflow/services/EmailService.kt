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

        val cc = bulkEmailRequest.cc
        val bcc = bulkEmailRequest.bcc

        val skippedEmails = mutableListOf<String>()
        var successCount = 0
        var failCount = 0
        var failedEmails = emptyList<String>()
        for(sendTo in bulkEmailRequest.to){
            val nonprofit = nonprofitService.findNonprofitByEmailAddress(sendTo)
            if (nonprofit == null) {
                skippedEmails.add(sendTo)
                failCount++
            } else {
                val email = buildEmail(bulkEmailRequest, nonprofit, sendTo, cc, bcc)

                if (cc.isNotEmpty()){
                    sendEmailToCC(bulkEmailRequest, nonprofit, cc, cc, bcc)
                    successCount++
                }

                if (bcc.isNotEmpty()){
                    sendEmailToBcc(bulkEmailRequest, nonprofit, bcc, cc, bcc)
                    successCount++
                }

                sendEmail(email)
                successCount++
            }
        }

        return BulkEmailResponse(
            successCount = successCount,
            failureCount = failCount,
            failedEmails = failedEmails,
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

    private fun buildEmail(bulkEmailRequest: BulkEmailRequest, nonprofit: Nonprofit, emailAddress: String, cc: String, bcc: String): Email {
        val processedBody = processEmailBody(bulkEmailRequest.body, nonprofit)
        return Email(to = emailAddress, from = bulkEmailRequest.from, subject = bulkEmailRequest.subject, body = processedBody, cc = cc, bcc = bcc)
    }

    private fun sendEmailToCC(bulkEmailRequest: BulkEmailRequest, nonprofit: Nonprofit, emailAddressReceiepnt: String, cc: String, bcc: String) {
        val ccEmail = buildEmail(bulkEmailRequest, nonprofit, cc, cc, bcc)
        sendEmail(ccEmail)
    }

    private fun sendEmailToBcc(bulkEmailRequest: BulkEmailRequest, nonprofit: Nonprofit, emailAddressReceiepnt: String, cc: String, bcc: String){
        val bccEmail = buildEmail(bulkEmailRequest, nonprofit, bcc, cc,bcc)
        sendEmail(bccEmail)
    }
}