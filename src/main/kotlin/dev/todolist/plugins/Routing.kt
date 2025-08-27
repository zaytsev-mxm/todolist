package dev.todolist.plugins

import dev.todolist.features.user.UserPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.swagger.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        swaggerUI(path = "swagger", swaggerFile = "src/main/resources/openapi/documentation.yaml") {
            version = "4.15.5"
        }

        authenticate("auth-jwt") {
            get("/jwt") {
                val p = call.principal<UserPrincipal>()
                call.respondText("Hello, ${p?.id}! You have these roles: ${p?.roles?.toString()}.")
            }
        }
    }
}
