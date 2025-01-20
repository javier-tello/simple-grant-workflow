package com.example.simpleGrantWorkflow.repositories

import com.example.simpleGrantWorkflow.models.Email

class InMemoryEmailRepository : EmailRepository {
    private val emailStorage = mutableListOf<Email>()

    override fun save(email: Email): Boolean {
        emailStorage.add(email)
        return true
    }

    override fun findAllEmails(): List<Email>{
        return emailStorage.toList()
    }
}