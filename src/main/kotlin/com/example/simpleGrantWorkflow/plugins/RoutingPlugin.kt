package com.example.simpleGrantWorkflow.plugins


import com.example.simpleGrantWorkflow.routing.emailRoutes
import com.example.simpleGrantWorkflow.routing.nonprofitRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        nonprofitRoutes()
        emailRoutes()
        get("/") {
            call.respondText("Hello World!")
        }
    }
}