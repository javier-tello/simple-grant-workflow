package com.example.simpleGrantWorkflow.services

import com.example.simpleGrantWorkflow.models.Nonprofit
import com.example.simpleGrantWorkflow.repositories.NonprofitRepository

class NonprofitService(private val nonprofitRepository: NonprofitRepository) {
    fun getAllNonprofits(): List<Nonprofit> = nonprofitRepository.findAll()

    fun createNonprofit(nonprofit: Nonprofit) = nonprofitRepository.save(nonprofit)

    fun findNonprofitByEmailAddress(emailAddress: String): Nonprofit? = nonprofitRepository.findNonprofitByEmailAddress(emailAddress)
}