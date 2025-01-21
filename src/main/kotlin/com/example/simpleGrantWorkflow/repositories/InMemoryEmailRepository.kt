package com.example.simpleGrantWorkflow.repositories

import com.example.simpleGrantWorkflow.models.Email

class InMemoryEmailRepository : EmailRepository {
    private val emailStorage = HashMap<String, Email>()

    override fun save(email: Email?): Boolean {
        if(email != null) {
            emailStorage[email.emailID] = email
            return true
        }else {
            return false
        }
    }

    override fun findAllEmails(): List<Email>{
        return emailStorage.values.toList()
    }
}