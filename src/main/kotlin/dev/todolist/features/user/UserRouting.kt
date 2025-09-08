package dev.todolist.features.user

import dev.todolist.features.auth.requireOwner
import dev.todolist.features.auth.requireRole
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureUserRouting(userController: UserController = UserController()) {
    routing {
        route("/users") {
            post {
                userController.addNewUser(call)
            }

            authenticate("auth-jwt") {
                get {
                    val userPrincipal = call.principal<UserPrincipal>()!!
                    call.requireRole(userPrincipal, "admin")
                    call.respondText(
                        "Hello, ${
                            (call.principal<JWTPrincipal>()?.payload?.getClaim("username")?.asString())
                        }! It seems you're admin!'"
                    )
                }

                route("/me") {
                    get {
                        userController.getCurrentUser(call)
                    }

                    patch {
                        userController.updateCurrentUser(call)
                    }
                }

                route("/{id}") {
                    get {
                        val userPrincipal = call.principal<UserPrincipal>()!!
                        call.requireOwner(userPrincipal)
                        userController.getUserById(call)
                    }

                    patch {
                        val userPrincipal = call.principal<UserPrincipal>()!!
                        call.requireOwner(userPrincipal)
                        userController.updateUserById(call)
                    }

                    delete {
                        val userPrincipal = call.principal<UserPrincipal>()!!
                        call.requireOwner(userPrincipal)
                        userController.removeUserById(call)
                    }
                }
            }
        }
    }
}