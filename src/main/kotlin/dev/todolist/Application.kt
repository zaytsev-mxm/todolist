package dev.todolist

import dev.todolist.features.login.configureLoginRouting
import dev.todolist.features.register.configureRegisterRouting
import dev.todolist.features.lists.configureListsRouting
import dev.todolist.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import org.jetbrains.exposed.sql.Database

fun main() {
    System.setProperty("io.ktor.development", "true")

    Database.connect(
        url = System.getenv("DB_URL") ?: "",
        driver = "org.postgresql.Driver",
        user = System.getenv("DB_USER") ?: "",
        password = System.getenv("DB_PASSWORD") ?: "",
    )
    embeddedServer(Netty, port = System.getenv("PORT").toInt(), host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }
    configureSerialization()
    // configureDatabases()
    configureRouting()
    configureLoginRouting()
    configureRegisterRouting()
    configureListsRouting()
}
