package dev.todolist.features.login

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureLoginRouting() {
    val loginController = LoginController()

    routing {
        post("/login") {
            loginController.performLogin(call)
        }
    }
}