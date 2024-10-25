package dev.todolist.database.users

import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users: Table("users".quote()) {
    private val login = Users.varchar("login".quote(), 50)
    private val password = Users.varchar("password".quote(), 50)
    private val username = Users.varchar("username".quote(), 50)
    private val email = Users.varchar("email".quote(), 50)

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[username] = userDTO.username
                it[email] = userDTO.email ?: ""
            }
        }
    }

    fun fetchUser(login: String): UserDTO? {
        return transaction {
            try {
                val userModel = Users.select { Users.login eq login }.singleOrNull()
                userModel?.let {
                    UserDTO(
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
    }
}