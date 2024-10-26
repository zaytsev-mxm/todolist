package dev.todolist

import dev.todolist.features.login.configureLoginRouting
import dev.todolist.features.register.configureRegisterRouting
import dev.todolist.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database

fun main() {
    Database.connect(
        url = System.getenv("DB_URL") ?: "",
        driver = "org.postgresql.Driver",
        user = System.getenv("DB_USER") ?: "",
        password = System.getenv("DB_PASSWORD") ?: "",
    )
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    // configureDatabases()
    configureRouting()
    configureLoginRouting()
    configureRegisterRouting()
}
