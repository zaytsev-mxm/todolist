package dev.todolist.features.register

import dev.todolist.database.tokens.TokenDTO
import dev.todolist.database.tokens.Tokens
import dev.todolist.database.users.UserDTO
import dev.todolist.database.users.Users
import dev.todolist.utils.isValidEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*

class RegisterController(
    private val call: ApplicationCall
) {
    suspend fun registerNewUser() {
        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
        if (!registerReceiveRemote.email.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Email is not valid")
        }

        val userDTO = Users.fetchUser(registerReceiveRemote.login)

        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        } else {
            val token = UUID.randomUUID().toString()

            try {
                Users.insert(UserDTO(
                    login = registerReceiveRemote.login,
                    password = registerReceiveRemote.password,
                    email = registerReceiveRemote.email,
                    username = ""
                ))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            }
            
            Tokens.insert(TokenDTO(
                rowId = UUID.randomUUID().toString(),
                login = registerReceiveRemote.login,
                token = token
            ))

            call.respond(RegisterResponseRemote(token = token))
        }
    }
}