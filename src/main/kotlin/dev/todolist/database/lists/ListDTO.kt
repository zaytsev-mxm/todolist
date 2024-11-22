package dev.todolist.database.lists

import kotlinx.serialization.Serializable

// Data-transfer object
@Serializable
class ListDTO(
    val id: String? = null,
    val title: String,
    val description: String? = null,
    val userId: String,
)