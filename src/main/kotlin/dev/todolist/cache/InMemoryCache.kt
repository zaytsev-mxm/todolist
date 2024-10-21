package dev.todolist.cache

import dev.todolist.features.register.RegisterReceiveRemote

data class TokenCache(
    val login: String,
    val token: String,
)

object InMemoryCache {
    val userList: MutableList<RegisterReceiveRemote> = mutableListOf()
    val token: MutableList<TokenCache> = mutableListOf()
}