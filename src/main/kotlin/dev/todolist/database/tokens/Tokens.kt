package dev.todolist.database.tokens

import io.ktor.http.*
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object Tokens: Table("tokens".quote()) {
    private val id = Tokens.varchar("id".quote(), 60)
    private val userId = Tokens.varchar("user_id".quote(), 60)
    private val token = Tokens.varchar("token".quote(), 60)

    fun insert(tokenDTO: TokenDTO) {
        transaction {
            Tokens.insert {
                it[id] = tokenDTO.id
                it[userId] = tokenDTO.userId
                it[token] = tokenDTO.token
            }
        }
    }
}