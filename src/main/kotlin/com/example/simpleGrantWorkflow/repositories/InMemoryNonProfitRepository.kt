package com.example.simpleGrantWorkflow.repositories

import com.example.simpleGrantWorkflow.models.Nonprofit

class InMemoryNonProfitRepository : NonprofitRepository {
    private val nonprofitStorage = mutableListOf<Nonprofit>()

    override fun save(nonprofit: Nonprofit): Boolean {
        nonprofitStorage.add(nonprofit)
        return true
    }

    override fun findAll(): List<Nonprofit> {
        return nonprofitStorage.toList()
    }

    override fun getAllEmailAddresses(): List<String> {
        val emailAddresses = mutableListOf<String>()
        for (nonprofit in nonprofitStorage) {
            emailAddresses.add(nonprofit.email)
        }
        return emailAddresses
    }
}