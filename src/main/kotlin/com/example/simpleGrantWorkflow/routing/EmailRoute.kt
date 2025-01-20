package com.example.simpleGrantWorkflow.routing

import com.example.simpleGrantWorkflow.models.Email
import com.example.simpleGrantWorkflow.models.Nonprofit
import com.example.simpleGrantWorkflow.repositories.InMemoryEmailRepository
import com.example.simpleGrantWorkflow.services.EmailService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.emailRoutes() {
    val emailService = EmailService(InMemoryEmailRepository())

    route("/emails") {
        get {
            val emails = emailService.findAllEmails()
            call.respond(HttpStatusCode.OK, emails)
        }
        post {
            val emailRequest = try {
                call.receive<Email>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid email format")
                return@post
            }

            val success = emailService.sendEmail(emailRequest)
            if (success) {
                call.respond(HttpStatusCode.Created, "Email successfully sent")
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Failed to send email")
            }
        }
    }
}

