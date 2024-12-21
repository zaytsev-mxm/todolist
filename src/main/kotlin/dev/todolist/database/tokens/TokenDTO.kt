package dev.todolist.database.tokens

import kotlinx.serialization.Serializable

@Serializable
class TokenDTO(
    val id: String,
    val userId: String,
    val token: String,
    val expiresAt: String? = null
)