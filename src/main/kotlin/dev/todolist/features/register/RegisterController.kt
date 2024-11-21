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

class RegisterController {
    suspend fun registerNewUser(call: ApplicationCall) {
        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
        if (!registerReceiveRemote.email.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Email is not valid")
            return
        }

        val userDTO = Users.fetchUser(registerReceiveRemote.login)

        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
            return
        } else {
            val token = UUID.randomUUID().toString()
            val tokenId = UUID.randomUUID().toString()
            val userId = UUID.randomUUID().toString()

            try {
                Users.insert(UserDTO(
                    id = userId,
                    login = registerReceiveRemote.login,
                    password = registerReceiveRemote.password,
                    email = registerReceiveRemote.email,
                    username = ""
                ))
            } catch (e: Exception) {
                println(e)
                call.respond(HttpStatusCode.InternalServerError, "Something went wrong")
                return
            }

            Tokens.insert(TokenDTO(
                id = tokenId,
                userId = userId,
                token = token
            ))

            call.respond(RegisterResponseRemote(token = token))
            return
        }
    }
}