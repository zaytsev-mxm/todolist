package dev.todolist.features.auth

import dev.todolist.features.user.UserPrincipal
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

suspend fun ApplicationCall.requireOwner(principal: UserPrincipal, param: String = "id") {
    if (parameters[param] != principal.id) {
        respond(HttpStatusCode.Forbidden, "Not your resource")
    }
}

suspend fun ApplicationCall.requireRole(principal: UserPrincipal, role: String) {
    if (role !in principal.roles) {
        respond(HttpStatusCode.Forbidden, "Missing role: $role")
    }
}