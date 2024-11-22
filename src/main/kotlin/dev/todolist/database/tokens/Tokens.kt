package dev.todolist.database.tokens

import dev.todolist.database.users.UserDTO
import dev.todolist.database.users.Users
import io.ktor.http.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Tokens: Table("tokens".quote()) {
    private val id = varchar("id".quote(), 60)
    private val userId = varchar("user_id".quote(), 60)
    private val token = varchar("token".quote(), 60)

    fun insert(tokenDTO: TokenDTO) {
        transaction {
            Tokens.insert {
                it[id] = tokenDTO.id
                it[userId] = tokenDTO.userId
                it[token] = tokenDTO.token
            }
        }
    }

    fun fetchToken(tokenValue: String): TokenDTO? {
        return transaction {
            try {
                val tokenModel = Tokens.selectAll().where { token eq tokenValue }.singleOrNull()
                tokenModel?.let {
                    TokenDTO(
                        id = it[Tokens.id],
                        userId = it[userId],
                        token = it[token],
                    )
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}