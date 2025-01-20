package com.example.simpleGrantWorkflow.services

import com.example.simpleGrantWorkflow.models.Email
import com.example.simpleGrantWorkflow.repositories.EmailRepository

class EmailService(private val emailRepository: EmailRepository) {
    fun sendEmail(email: Email): Boolean = emailRepository.save(email)
    fun findAllEmails(): List<Email> = emailRepository.findAllEmails()
}