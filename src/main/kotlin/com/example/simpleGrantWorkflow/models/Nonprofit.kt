package com.example.simpleGrantWorkflow.models

import kotlinx.serialization.Serializable

@Serializable
data class Nonprofit(
    val name: String,
    val email: String,
    val address: String
)