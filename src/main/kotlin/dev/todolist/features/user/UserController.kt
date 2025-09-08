package dev.todolist.features.user

import com.auth0.jwt.interfaces.Payload
import dev.todolist.database.users.UserDTO
import dev.todolist.database.users.Users
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond

data class UserPrincipal(
    val userId: String? = null,
    val userName: String? = null,
    val roles: Set<String> = emptySet(),
    val jwtPayload: Payload? = null
)

class UserController {
    suspend fun getCurrentUser(call: ApplicationCall) {
        val userPrincipal = call.principal<UserPrincipal>()!!
        val user = Users.fetch(id = userPrincipal.userId)

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

        val userDTO = Users.fetch(id = id)

        if (userDTO != null) {
            call.respond(HttpStatusCode.OK, UserResponseRemote(userDTO))
        } else {
            call.respond(HttpStatusCode.NotFound, "User not found")
        }
    }

    suspend fun updateCurrentUser(call: ApplicationCall) {
        val userPrincipal = call.principal<UserPrincipal>()!!

        val user = Users.fetch(id = userPrincipal.userId)

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