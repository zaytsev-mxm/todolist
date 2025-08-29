package dev.todolist.plugins

import dev.todolist.features.user.UserPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
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
                val expiresAt = p?.jwtPayload?.expiresAt?.time?.minus(System.currentTimeMillis())
                val name = p?.userName ?: p?.userId ?: "Unknown"
                call.respondText("Hello, ${name}! You have these roles: ${p?.roles?.toString()}. Token is expired at $expiresAt ms.")
            }
        }
    }
}
