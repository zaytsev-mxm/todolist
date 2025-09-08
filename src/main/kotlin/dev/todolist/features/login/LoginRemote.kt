package dev.todolist.features.login

import dev.todolist.database.users.UserDTO
import kotlinx.serialization.Serializable

@Serializable
data class LoginReceiveRemote(
    val login: String,
    val password: String,
)

@Serializable
data class LoginResponseRemote(
    val jwt: String,
    val user: UserDTO,
)
