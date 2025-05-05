package dev.todolist.features.user

import io.ktor.server.application.Application
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureUserRouting() {
    val userController = UserController()

    routing {
        get("/user") {
            userController.getCurrentUser(call)
        }

        get("/user/{id}") {
            userController.getUserById(call)
        }

        patch("/user") {
            userController.updateCurrentUser(call)
        }

        patch("/user/{id}") {
            userController.updateUserById(call)
        }

        post("/user") {
            userController.addNewUser(call)
        }

        delete("/user/{id}") {
            userController.removeUserById(call)
        }
    }
}