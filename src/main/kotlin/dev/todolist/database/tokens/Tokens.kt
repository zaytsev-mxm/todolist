package dev.todolist.database.tokens

import io.ktor.http.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit


object Tokens : Table("tokens".quote()) {
    private val id = varchar("id".quote(), 60)
    private val userId = varchar("user_id".quote(), 60)
    private val token = varchar("token".quote(), 256)
    private val expiresAt = datetime("expires_at".quote())

    fun insert(tokenDTO: TokenDTO) {
        val expiresAtValue = Instant.now().plus(7, ChronoUnit.MINUTES).toString()
        val instant = Instant.parse(expiresAtValue)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

        transaction {
            Tokens.insert {
                it[id] = tokenDTO.id
                it[userId] = tokenDTO.userId
                it[token] = tokenDTO.token
                it[expiresAt] = localDateTime
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
                        expiresAt = it[expiresAt].toString()
                    )
                }
            } catch (_: Exception) {
                null
            }
        }
    }

    fun cleanTokens() {
        transaction {
            val localDateTime = LocalDateTime.now()
            Tokens.deleteWhere { expiresAt lessEq localDateTime }
        }
    }
}