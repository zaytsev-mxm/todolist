package dev.todolist.features.lists

import dev.todolist.database.lists.*
import dev.todolist.database.tokens.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class ListsController {
    suspend fun addList(call: ApplicationCall) {
        val tokenDTO = getToken(call)

        if (tokenDTO == null) {
            call.respond(HttpStatusCode.Unauthorized)
            return
        }

        val listReceiveRemote = call.receive<ListsReceiveRemote>()

        val listDTO = Lists.insert(ListDTO(
            title = listReceiveRemote.title,
            description = listReceiveRemote.description,
            userId = tokenDTO.userId
        ))

        if (listDTO == null) {
            call.respond(HttpStatusCode.InternalServerError)
        } else {
            call.respond(HttpStatusCode.OK, listDTO)
        }
    }

    suspend fun getUserLists(call: ApplicationCall) {
        val tokenDTO = getToken(call)

        if (tokenDTO == null) {
            call.respond(HttpStatusCode.Unauthorized)
            return
        }

        val lists = Lists.fetchListsForUser(tokenDTO.userId)

        if (lists == null) {
            call.respond(HttpStatusCode.InternalServerError)
            return
        }

        call.respond(HttpStatusCode.OK, lists)
    }

    fun getListById(call: ApplicationCall) {
        TODO()
    }

    fun updateList(call: ApplicationCall) {
        TODO()
    }

    fun removeListById(call: ApplicationCall) {
        TODO()
    }

    private fun getToken(call: ApplicationCall): TokenDTO? {
        val cookies = call.request.cookies
        val tokenCookie = cookies["token"] ?: return null
        val tokenDTO = Tokens.fetchToken(tokenCookie)

        return tokenDTO
    }
}