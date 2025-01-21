package com.example.simpleGrantWorkflow.models

import kotlinx.serialization.Serializable

@Serializable
data class BulkEmailResponse(
    val successCount: Int,
    val failureCount: Int,
    val failedEmails: List<String>,
    val message: String,
    val skippedEmails: List<String>
)