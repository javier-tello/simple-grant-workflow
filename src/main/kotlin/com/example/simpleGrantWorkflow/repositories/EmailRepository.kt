package com.example.simpleGrantWorkflow.repositories

import com.example.simpleGrantWorkflow.models.Email

interface EmailRepository {
    fun save(email: Email?): Boolean
    fun findAllEmails(): List<Email>
}