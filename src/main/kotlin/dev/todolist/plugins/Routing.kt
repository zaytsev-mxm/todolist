package dev.todolist.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.swagger.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/lenochka") {
            call.respondText { "Losho4ek" }
        }

        swaggerUI(path = "swagger", swaggerFile = "src/main/resources/openapi/documentation.yaml") {
            version = "4.15.5"
        }
    }
}