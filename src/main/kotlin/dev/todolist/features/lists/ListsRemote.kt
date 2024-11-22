package dev.todolist.features.lists

import kotlinx.serialization.Serializable

@Serializable
data class ListsReceiveRemote(
    val title: String,
    val description: String? = null,
)

@Serializable
data class ListsResponseRemote(
    val id: String,
    val title: String,
    val description: String? = null,
)
