package com.example.simpleGrantWorkflow.plugins

import com.example.simpleGrantWorkflow.routing.nonprofitRoutes
import com.example.simpleGrantWorkflow.services.EmailService
import com.example.simpleGrantWorkflow.services.NonprofitService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(nonprofitService: NonprofitService, emailService: EmailService) {
    routing {
        nonprofitRoutes(nonprofitService, emailService)
        get("/") {
            call.respondText("Hello World!")
        }
    }
}