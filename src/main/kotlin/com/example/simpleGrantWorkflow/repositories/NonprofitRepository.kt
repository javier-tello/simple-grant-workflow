package com.example.simpleGrantWorkflow.repositories

import com.example.simpleGrantWorkflow.models.Nonprofit

interface NonprofitRepository {
    fun save(nonprofit: Nonprofit): Boolean
    fun findAll(): List<Nonprofit>
    fun getAllEmailAddresses(): List<String>
}