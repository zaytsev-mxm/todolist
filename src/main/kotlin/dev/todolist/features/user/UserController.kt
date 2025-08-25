package dev.todolist.features.user

import dev.todolist.database.users.UserDTO
import dev.todolist.database.users.Users
import dev.todolist.features.common.RouteController
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

data class UserPrincipal(
    val id: String? = null,
    val roles: Set<String> = emptySet()
)

class UserController : RouteController() {
    suspend fun getCurrentUser(call: ApplicationCall) {
        val tokenDTO = getToken(call)

        if (tokenDTO == null) {
            call.respond(HttpStatusCode.Unauthorized)
            return
        }

        val user = Users.fetchUser(id = tokenDTO.userId)

        if (user != null) {
            call.respond(HttpStatusCode.OK, UserResponseRemote(user))
        } else {
            call.respond(HttpStatusCode.NotFound, "User was not found")
        }
    }

    suspend fun getUserById(call: ApplicationCall) {
        val id = call.parameters["id"]

        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Add 'id' to the GET-parameters")
            return
        }

        val user = Users.fetchUser(id = id)

        if (user != null) {
            call.respond(HttpStatusCode.OK, UserResponseRemote(user))
        } else {
            call.respond(HttpStatusCode.NotFound, "User not found")
        }
    }

    suspend fun updateCurrentUser(call: ApplicationCall) {
        val tokenDTO = getToken(call)

        if (tokenDTO == null) {
            call.respond(HttpStatusCode.Unauthorized)
            return
        }

        val user = Users.fetchUser(id = tokenDTO.userId)

        val userDTO = call.receive<UserDTO>()

        if (user != null) {
            try {
                val newUser = user.copy(username = userDTO.username)
                Users.update(newUser)
                call.respond(HttpStatusCode.OK, UserResponseRemote(newUser))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.PaymentRequired, "Invalid user data: ${e.message}")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred while updating the user: ${e.message}")
            }
        } else {
            call.respond(HttpStatusCode.BadRequest, "Invalid input or user not found")
        }
    }

    suspend fun updateUserById(call: ApplicationCall) {}

    suspend fun addNewUser(call: ApplicationCall) {}

    suspend fun removeUserById(call: ApplicationCall) {}
}