package com.example.simpleGrantWorkflow

import com.example.simpleGrantWorkflow.plugins.configureRouting
import com.example.simpleGrantWorkflow.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureRouting()
    configureSerialization()
}
