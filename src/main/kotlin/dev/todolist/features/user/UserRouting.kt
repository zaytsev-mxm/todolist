package dev.todolist.features.user

import dev.todolist.features.auth.requireOwner
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
        authenticate("auth-jwt") {
            get("/jwt") {
                val p = call.principal<UserPrincipal>()
                val principal = call.principal<JWTPrincipal>()
                val userid = principal?.payload?.getClaim("userid")?.asString()
                print(p.toString())
                val expiresAt = principal?.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, ${p?.id}! Token is expired at $expiresAt ms.")
            }

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
                        // val p = call.principal<UserPrincipal>()!!
                        // call.requireOwner(p)
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