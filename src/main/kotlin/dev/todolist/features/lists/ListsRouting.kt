package dev.todolist.features.lists

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureListsRouting() {
    val listsController = ListsController()

    routing {
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