package com.example.simpleGrantWorkflow.routing

import com.example.simpleGrantWorkflow.models.BulkEmailRequest
import com.example.simpleGrantWorkflow.models.Email
import com.example.simpleGrantWorkflow.models.Nonprofit
import com.example.simpleGrantWorkflow.services.EmailService
import com.example.simpleGrantWorkflow.services.NonprofitService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

fun Route.nonprofitRoutes(nonprofitService: NonprofitService, emailService: EmailService) {
    val logger = LoggerFactory.getLogger("Application")

    route("/nonprofits") {
        get {
            logger.info("Received GET request at /nonprofits")
            val nonprofits = nonprofitService.getAllNonprofits()
            call.respond(HttpStatusCode.OK, nonprofits)
        }
        post {
            logger.info("Received POST request at /nonproftis")
            val nonprofitRequest = try {
                call.receive<Nonprofit>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid nonprofit format")
                return@post
            }

            nonprofitService.createNonprofit(nonprofitRequest)
            call.respond(HttpStatusCode.Created, "Nonprofit successfully created")
        }
    }

    route("/send-email") {
        post {
            logger.info("Received POST request at /send-email")
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
        post("/bulk"){
            logger.info("Received POST request at /send-email/bulk")
            val bulkEmailRequest = try {
                call.receive<BulkEmailRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid bulk email format")
                return@post
            }

            val response = emailService.processAndSendBulkEmails(bulkEmailRequest, nonprofitService)
            call.respond(HttpStatusCode.Created, response)
        }
    }
    route("/get-all-emails"){
        get {
            logger.info("Received GET request at /get-all-emails")
            val emails = emailService.findAllEmails()
            call.respond(HttpStatusCode.OK, emails)
        }
    }
}

