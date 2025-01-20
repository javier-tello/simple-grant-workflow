package com.example.simpleGrantWorkflow.routing

import com.example.simpleGrantWorkflow.models.Nonprofit
import com.example.simpleGrantWorkflow.repositories.InMemoryNonProfitRepository
import com.example.simpleGrantWorkflow.services.NonprofitService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.nonprofitRoutes() {
    val nonprofitService = NonprofitService(InMemoryNonProfitRepository())

    route("/nonprofits") {
        get {
            val nonprofits = nonprofitService.getAllNonprofits()
            call.respond(HttpStatusCode.OK, nonprofits)
        }
        post {
            val nonprofitRequest = try {
                call.receive<Nonprofit>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid nonprofit format")
                return@post
            }

            val success = nonprofitService.createNonprofit(nonprofitRequest)
            if (success) {
                call.respond(HttpStatusCode.Created, "Nonprofit successfully created")
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Failed to create nonprofit")
            }
        }
    }
}

