package dev.todolist.database.users

import io.ktor.http.*
import org.jetbrains.exposed.sql.*
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

    fun fetchUser(login: String? = null, id: String? = null): UserDTO? {
        if (login != null) {
            return transaction {
                try {
                    val userModel = Users.selectAll().where { Users.login eq login }.singleOrNull()
                    userModel?.let {
                        UserDTO(
                            id = it[Users.id],
                            login = it[Users.login],
                            password = it[Users.password],
                            username = it[Users.username],
                            email = it[Users.email],
                        )
                    }
                } catch (e: Exception) {
                    null
                }
            }
        } else if (id != null) {
            return transaction {
                try {
                    val userModel = Users.selectAll().where { Users.id eq id }.singleOrNull()
                    userModel?.let {
                        UserDTO(
                            id = it[Users.id],
                            login = it[Users.login],
                            password = it[Users.password],
                            username = it[Users.username],
                            email = it[Users.email],
                        )
                    }
                } catch (e: Exception) {
                    null
                }
            }
        } else {
            return null
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