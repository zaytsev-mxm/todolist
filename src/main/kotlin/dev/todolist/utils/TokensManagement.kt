package dev.todolist.utils
import dev.todolist.database.tokens.Tokens
import kotlinx.coroutines.*

object TokensManagement {
    private const val DEFAULT_CLEAN_UP_INTERVAL = 24 * 60 * 60 * 1000L; // Run once a day

    private val cleanupScope = CoroutineScope(Dispatchers.Default + Job())

    fun startTokenCleanupJob(interval: Long = DEFAULT_CLEAN_UP_INTERVAL) {
        cleanupScope.launch {
            while (isActive) {
                Tokens.cleanTokens()
                delay(interval)
            }
        }
    }

    fun stopTokenCleanupJob() {
        cleanupScope.cancel() // Stop the coroutine when needed
    }
}

object TokenConstants {
    val secret = System.getenv("JWT_SECRET") ?: ""
    val issuer = System.getenv("JWT_ISSUER") ?: ""
    val audience = System.getenv("JWT_AUDIENCE") ?: ""
    val realm = System.getenv("JWT_REALM") ?: ""
}