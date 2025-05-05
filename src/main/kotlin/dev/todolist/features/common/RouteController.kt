package dev.todolist.features.common

import dev.todolist.database.tokens.TokenDTO
import dev.todolist.database.tokens.Tokens
import io.ktor.server.application.ApplicationCall

open class RouteController {
    protected fun getToken(call: ApplicationCall): TokenDTO? {
        val cookies = call.request.cookies
        val tokenCookie = cookies["token"] ?: return null
        val tokenDTO = Tokens.fetchToken(tokenCookie)

        return tokenDTO
    }
}