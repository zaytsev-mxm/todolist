package dev.todolist.database.users

import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.crypto.bcrypt.BCrypt

object Users: Table("users".quote()) {
    private val id = Users.varchar("id".quote(), 60)
    private val login = Users.varchar("login".quote(), 60)
    private val password = Users.varchar("password".quote(), 60)
    private val username = Users.varchar("username".quote(), 30)
    private val email = Users.varchar("email".quote(), 30)

    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun insert(userDTO: UserDTO) {
        val passedPassword = userDTO.password ?: throw IllegalArgumentException("Password cannot be null")

        transaction {
            val hashedPassword = hashPassword(passedPassword)

            Users.insert {
                it[id] = userDTO.id ?: throw IllegalArgumentException("User ID cannot be null")
                it[login] = userDTO.login ?: ""
                it[password] = hashedPassword
                it[username] = userDTO.username ?: ""
                it[email] = userDTO.email ?: ""
            }
        }
    }

    private fun toDTO(row: ResultRow): UserDTO =
        UserDTO(
            id = row[Users.id],
            login = row[Users.login],
            password = row[Users.password],
            username = row[Users.username],
            email = row[Users.email],
        )

    fun fetch(login: String? = null, id: String? = null): UserDTO? {
        val condition = when {
            login != null -> Users.login eq login
            id != null -> Users.id eq id
            else -> return null
        }

        return transaction {
            try {
                Users.selectAll().where { condition }
                    .singleOrNull()
                    ?.let(::toDTO)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun update(userDTO: UserDTO) {
        transaction {
            val id = userDTO.id ?: ""
            val userName = userDTO.username ?: ""
            Users.update({ Users.id eq id }) {
                it[Users.username] = userName
            }
        }
    }
}