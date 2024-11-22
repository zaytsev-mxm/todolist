package dev.todolist.database.lists

import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object Lists : Table("lists".quote()) {
    private val id = Lists.varchar("id".quote(), 60)
    private val title = Lists.varchar("title".quote(), 150)
    private val description = Lists.varchar("description".quote(), 500)
    private val userId = Lists.varchar("user_id".quote(), 60)

    fun insert(listDTO: ListDTO): ListDTO? {
        return transaction {
            try {
                val id = UUID.randomUUID().toString()
                Lists.insert {
                    it[Lists.id] = id
                    it[title] = listDTO.title
                    it[description] = listDTO.description ?: ""
                    it[userId] = listDTO.userId
                }
                return@transaction ListDTO(
                    id = id,
                    title = listDTO.title,
                    description = listDTO.description ?: "",
                    userId = listDTO.userId,
                )
            } catch (e: Exception) {
                return@transaction null
            }
        }
    }

    fun fetchListsForUser(userId: String): List<ListDTO>? {
        return transaction {
            try {
                Lists.selectAll().where { Lists.userId eq userId }.map {
                    ListDTO(
                        id = it[Lists.id],
                        title = it[Lists.title],
                        description = it[Lists.description],
                        userId = it[Lists.userId]
                    )
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}
