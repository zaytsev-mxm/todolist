package dev.todolist

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.todolist.database.users.Users
import dev.todolist.features.login.configureLoginRouting
import dev.todolist.features.register.configureRegisterRouting
import dev.todolist.features.lists.configureListsRouting
import dev.todolist.features.user.UserPrincipal
import dev.todolist.features.user.configureUserRouting
import dev.todolist.utils.TokensManagement
import dev.todolist.plugins.*
import dev.todolist.utils.TokenConstants
import io.ktor.http.*
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.respond
import dev.todolist.database.DatabaseFactory

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT_APP").toInt(), host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // Initialize a database with schema creation
    DatabaseFactory.init()

    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }

    install(Authentication) {
        jwt("auth-jwt") {
            realm = TokenConstants.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(TokenConstants.secret))
                    .withAudience(TokenConstants.audience)
                    .withIssuer(TokenConstants.issuer)
                    .build()
            )
            // Extract JWT from a cookie named "token"
            // (by default, Ktor expects it to be sent as an "Authorization: Bearer ...value" header)
            authHeader { call ->
                call.request.cookies["token"]?.let { token ->
                    HttpAuthHeader.Single("Bearer", token)
                }
            }
            validate { credential ->
                val userId = credential.payload.getClaim("userid").asString().orEmpty()
                if (userId.isBlank()) return@validate null

                // Single DB fetch with minimal fields
                val userDTO = Users.fetch(id = userId) ?: return@validate null
                UserPrincipal(
                    userId = userDTO.id,
                    userName = userDTO.username,
                    roles = userDTO.roles,
                    jwtPayload = credential.payload
                ) // no more DB in handlers
            }
            challenge { defaultScheme, realm ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    "Token is not valid or has expired. DefaultScheme: $defaultScheme, realm: $realm"
                )
            }
        }
    }

    // Utils:
    configureSerialization()

    // Routes:
    configureBasicRouting()
    configureLoginRouting()
    configureRegisterRouting()
    configureListsRouting()
    configureUserRouting()

    TokensManagement.startTokenCleanupJob()
}
