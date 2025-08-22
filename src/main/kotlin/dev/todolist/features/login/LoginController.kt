package dev.todolist.features.login

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.todolist.database.tokens.TokenDTO
import dev.todolist.database.tokens.Tokens
import dev.todolist.database.users.Users
import dev.todolist.utils.TokenConstants
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
            with (userDTO.id ?: throw IllegalArgumentException("User id cannot be null")) {
                val storedHashedPassword = userDTO.password
                if (BCrypt.checkpw(loginReceiveRemote.password, storedHashedPassword)) {
                    val jwtString = makeJwtString(userDTO.id)
                    Tokens.insert(TokenDTO(
                        id = UUID.randomUUID().toString(),
                        userId = userDTO.id,
                        token = jwtString
                    ))
                    call.response.cookies.append(Cookie("token", jwtString))
                    call.respond(LoginResponseRemote(jwtString, userDTO))
                    return
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid password")
                    return
                }
            }
        }
    }

    private fun makeJwtString(userid: String): String {
        val token = JWT.create()
            .withAudience(TokenConstants.audience)
            .withIssuer(TokenConstants.issuer)
            .withClaim("userid", userid)
            .withClaim("zhopabobra", "votonavotona")
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(TokenConstants.secret))

        return token
    }
}