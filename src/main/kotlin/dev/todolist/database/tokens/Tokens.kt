package dev.todolist.database.tokens

import io.ktor.http.*
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object Tokens: Table("tokens".quote()) {
    private val id = Tokens.varchar("id".quote(), 50)
    private val login = Tokens.varchar("login".quote(), 50)
    private val token = Tokens.varchar("token".quote(), 50)

    fun insert(tokenDTO: TokenDTO) {
        transaction {
            Tokens.insert {
                it[id] = tokenDTO.rowId
                it[login] = tokenDTO.login
                it[token] = tokenDTO.token
            }
        }
    }
}