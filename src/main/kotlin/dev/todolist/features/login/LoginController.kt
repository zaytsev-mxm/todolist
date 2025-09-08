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
        val userDTO = Users.fetch(loginReceiveRemote.login)

        if (userDTO == null) {
            call.respond(HttpStatusCode.NotFound, "User not found")
        } else {
            // @TODO - we need this guard because the UserDTO has the "id" field marked as nullable
            // We need to learn the actual use-cases for the UserDTO, because currently
            // having all its fields nullable doesn't make much sense
            if (userDTO.id == null) throw IllegalArgumentException(
                "User id is null"
            )
            if (BCrypt.checkpw(loginReceiveRemote.password, userDTO.password)) {
                val jwtString = makeJwtString(userDTO.id)
                val tokenDTO = TokenDTO(
                    id = UUID.randomUUID().toString(),
                    userId = userDTO.id,
                    token = jwtString
                )
                Tokens.insert(tokenDTO)
                call.response.cookies.append(Cookie("token", jwtString))
                call.respond(LoginResponseRemote(jwtString, userDTO))
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid password")
            }
        }
    }

    private fun makeJwtString(userid: String): String {
        val time30min = 30 * 60 * 1000
        val expiresAt = System.currentTimeMillis() + time30min
        val token = JWT.create()
            .withAudience(TokenConstants.audience)
            .withIssuer(TokenConstants.issuer)
            .withClaim("userid", userid)
            .withClaim("roles", listOf<String>())
            .withExpiresAt(Date(expiresAt))
            .sign(Algorithm.HMAC256(TokenConstants.secret))

        return token
    }
}