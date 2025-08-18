package dev.todolist

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.todolist.features.login.configureLoginRouting
import dev.todolist.features.register.configureRegisterRouting
import dev.todolist.features.lists.configureListsRouting
import dev.todolist.features.user.configureUserRouting
import dev.todolist.utils.TokensManagement
import dev.todolist.plugins.*
import dev.todolist.utils.TokenConstants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.sql.Database

fun main() {
    Database.connect(
        url = getDbUrl(),
        driver = "org.postgresql.Driver",
        user = System.getenv("DB_USER") ?: "",
        password = System.getenv("DB_PASSWORD") ?: "",
    )
    embeddedServer(Netty, port = System.getenv("PORT_APP").toInt(), host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun getDbUrl(): String {
    val dbHost = System.getenv("DB_HOST") ?: ""
    val portDb = System.getenv("PORT_DB") ?: ""
    val dbName = System.getenv("DB_NAME") ?: ""
    return "jdbc:postgresql://${dbHost}:${portDb}/${dbName}"
}

fun Application.module() {
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }

    /**
     * TODO: red those values from config
     */
    /*val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()*/
    val secret = TokenConstants.secret
    val issuer = TokenConstants.issuer
    val audience = TokenConstants.audience
    val myRealm = TokenConstants.realm
    install(Authentication) {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(JWT
                .require(Algorithm.HMAC256(TokenConstants.secret))
                .withAudience(TokenConstants.audience)
                .withIssuer(TokenConstants.issuer)
                .build()
            )
        }
    }

    // Utils:
    configureSerialization()

    // Routes:
    configureRouting()
    configureLoginRouting()
    configureRegisterRouting()
    configureListsRouting()
    configureUserRouting()

    TokensManagement.startTokenCleanupJob()
}
