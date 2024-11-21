package dev.todolist.database.tokens

import kotlinx.serialization.Serializable

// Data-transfer object
@Serializable
class TokenDTO(
    val id: String,
    val userId: String,
    val token: String,
)