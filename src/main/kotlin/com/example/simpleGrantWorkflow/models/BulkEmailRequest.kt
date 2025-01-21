package com.example.simpleGrantWorkflow.models

import kotlinx.serialization.Serializable

@Serializable
data class BulkEmailRequest(
    val to: List<String>,
    val from: String,
    val subject: String,
    val body: String
)
