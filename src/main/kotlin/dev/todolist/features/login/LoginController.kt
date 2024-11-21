package dev.todolist.features.login

import dev.todolist.database.tokens.TokenDTO
import dev.todolist.database.tokens.Tokens
import dev.todolist.database.users.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.springframework.security.crypto.bcrypt.BCrypt
import java.util.*

class LoginController {
    suspend fun performLogin(call: ApplicationCall) {
        val loginReceiveRemote = call.receive<LoginReceiveRemote>()
        val userDTO = Users.fetchUser(loginReceiveRemote.login)

        if (userDTO == null) {
            call.respond(HttpStatusCode.NotFound, "User not found")
            return
        } else {
            val storedHashedPassword = userDTO.password
            if (BCrypt.checkpw(loginReceiveRemote.password, storedHashedPassword)) {
                val token = UUID.randomUUID().toString()
                Tokens.insert(TokenDTO(
                    id = UUID.randomUUID().toString(),
                    userId = userDTO.id ?: throw IllegalArgumentException("User id cannot be null"),
                    token = token
                ))

                call.response.cookies.append(Cookie("token", token))
                call.respond(LoginResponseRemote(token = token))
                return
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
                return
            }
        }
    }
}