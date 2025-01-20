package com.example.simpleGrantWorkflow.models

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Email(
    val emailID: String = UUID.randomUUID().toString(),
    val to: String,
    val from: String,
    val subject: String,
    val body: String
)
