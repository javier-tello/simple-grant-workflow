package com.example.simpleGrantWorkflow.models

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Nonprofit(
    val nonprofitID: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val address: String
)