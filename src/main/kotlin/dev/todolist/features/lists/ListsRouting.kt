package dev.todolist.features.lists

import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respondText
import io.ktor.server.routing.*

fun Application.configureListsRouting() {
    val listsController = ListsController()

    routing {
        authenticate("auth-jwt") {
            get("/jwt") {
                val principal = call.principal<JWTPrincipal>()
                val userid = principal!!.payload.getClaim("userid").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $userid! Token is expired at $expiresAt ms.")
            }
        }
        get("/lists") {
            listsController.getUserLists(call)
        }

        get("/lists/{id}") {
            listsController.getListById(call)
        }

        patch("/lists/{id}") {
            listsController.updateList(call)
        }

        post("/lists") {
            listsController.addList(call)
        }

        delete("/lists/{id}") {
            listsController.removeListById(call)
        }
    }
}