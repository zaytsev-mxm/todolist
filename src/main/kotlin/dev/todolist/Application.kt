package dev.todolist

import dev.todolist.features.login.configureLoginRouting
import dev.todolist.features.register.configureRegisterRouting
import dev.todolist.features.lists.configureListsRouting
import dev.todolist.features.user.configureUserRouting
import dev.todolist.utils.TokensManagement
import dev.todolist.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
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
