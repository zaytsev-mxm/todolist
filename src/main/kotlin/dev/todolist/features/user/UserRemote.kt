package dev.todolist.features.user

import dev.todolist.database.users.UserDTO
import kotlinx.serialization.Serializable

@Serializable
data class UserReceiveRemote(
    val user: UserDTO?,
)

@Serializable
data class UserResponseRemote(
    val user: UserDTO?,
)
