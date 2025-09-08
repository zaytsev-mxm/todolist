package dev.todolist.database.users

import kotlinx.serialization.Serializable

// Data-transfer object
@Serializable
data class UserDTO(
    val id: String? = null,
    val login: String? = null,
    val password: String? = null,
    val email: String? = null,
    val username: String? = null,
    var roles: Set<String> = emptySet()
)