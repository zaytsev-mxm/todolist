package dev.todolist.database.users

import kotlinx.serialization.Serializable

// Data-transfer object
@Serializable
class UserDTO(
    val id: String? = null,
    val login: String,
    val password: String? = null,
    val email: String?,
    val username: String?,
)