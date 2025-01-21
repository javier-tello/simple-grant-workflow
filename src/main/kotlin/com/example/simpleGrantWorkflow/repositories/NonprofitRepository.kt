package com.example.simpleGrantWorkflow.repositories

import com.example.simpleGrantWorkflow.models.Nonprofit

interface NonprofitRepository {
    fun save(nonprofit: Nonprofit)
    fun findAll(): List<Nonprofit>
    fun findNonprofitByEmailAddress(emailAddress: String): Nonprofit?
}