package com.example.simpleGrantWorkflow.services

import com.example.simpleGrantWorkflow.models.Nonprofit
import com.example.simpleGrantWorkflow.repositories.NonprofitRepository

class NonprofitService(private val nonprofitRepository: NonprofitRepository) {
    fun getAllNonprofits(): List<Nonprofit> = nonprofitRepository.findAll()
    fun getAllEmailAddresses(): List<String> = nonprofitRepository.getAllEmailAddresses()
    fun createNonprofit(nonprofit: Nonprofit): Boolean = nonprofitRepository.save(nonprofit)
}