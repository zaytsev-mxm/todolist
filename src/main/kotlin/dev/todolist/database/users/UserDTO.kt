package dev.todolist.database.users

// Data-transfer object
class UserDTO(
    val login: String,
    val password: String,
    val email: String?,
    val username: String,
)