package dev.todolist.features.login

import dev.todolist.database.tokens.TokenDTO
import dev.todolist.database.tokens.Tokens
import dev.todolist.database.users.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*

class LoginController(
    private val call: ApplicationCall
) {
    suspend fun performLogin() {
        val loginReceiveRemote = call.receive<LoginReceiveRemote>()
        val userDTO = Users.fetchUser(loginReceiveRemote.login)

        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
        } else {
            if (userDTO.password == loginReceiveRemote.password) {
                val token = UUID.randomUUID().toString()
                Tokens.insert(
                    TokenDTO(
                        rowId = UUID.randomUUID().toString(),
                        login = loginReceiveRemote.login,
                        token = token
                    )
                )

                call.respond(LoginResponseRemote(token = token))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
        }
    }
}