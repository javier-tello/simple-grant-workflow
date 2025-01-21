package com.example.simpleGrantWorkflow.repositories

import com.example.simpleGrantWorkflow.models.Nonprofit

class InMemoryNonProfitRepository : NonprofitRepository {
    private val nonprofitStorage = hashMapOf<String, Nonprofit>()

    override fun save(nonprofit: Nonprofit) {
        nonprofitStorage[nonprofit.email] = nonprofit
    }

    override fun findAll(): List<Nonprofit> {
        return nonprofitStorage.values.toList()
    }


    override fun findNonprofitByEmailAddress(emailAddress: String): Nonprofit? {
        return nonprofitStorage.getOrDefault(emailAddress, null)
    }
}