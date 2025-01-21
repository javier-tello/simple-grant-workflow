package com.example.simpleGrantWorkflow

import com.example.simpleGrantWorkflow.plugins.configureRouting
import com.example.simpleGrantWorkflow.plugins.configureSerialization
import com.example.simpleGrantWorkflow.repositories.InMemoryEmailRepository
import com.example.simpleGrantWorkflow.repositories.InMemoryNonProfitRepository
import com.example.simpleGrantWorkflow.services.EmailService
import com.example.simpleGrantWorkflow.services.NonprofitService
import com.example.simpleGrantWorkflow.utils.TemplateProcessor
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val nonprofitRepository = InMemoryNonProfitRepository()
    val emailRepository = InMemoryEmailRepository()

    val processor = TemplateProcessor()
    val nonprofitService = NonprofitService(nonprofitRepository)
    val emailService = EmailService(emailRepository, processor)

    configureRouting(nonprofitService, emailService)
    configureSerialization()
}
