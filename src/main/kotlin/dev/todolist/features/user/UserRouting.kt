package dev.todolist.features.user

import io.ktor.server.application.Application
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureUserRouting(userController: UserController = UserController()) {
    routing {
        route("/user") {
            get {
                userController.getCurrentUser(call)
            }

            patch {
                userController.updateCurrentUser(call)
            }

            post {
                userController.addNewUser(call)
            }

            route("/{id}") {
                get {
                    userController.getUserById(call)
                }

                patch {
                    userController.updateUserById(call)
                }

                delete {
                    userController.removeUserById(call)
                }
            }
        }
    }
}