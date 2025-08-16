package dev.todolist.features.user

import io.ktor.server.application.Application
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.auth.authenticate

fun Application.configureUserRouting(
    userController: UserController = UserController()
) {
    routing {
        // Apply auth to all user routes if appropriate
        authenticate {
            route("/users") {
                // Current user endpoints should live under /users/me
                route("/me") {
                    get {
                        userController.getCurrentUser(call)
                    }
                    patch {
                        userController.updateCurrentUser(call)
                    }
                }

                // Decide whether to keep POST /users or rely on /register (authorization may be required)
                post {
                    userController.addNewUser(call)
                }

                // Item endpoints
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
}