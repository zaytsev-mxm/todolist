package dev.todolist.utils
import dev.todolist.database.tokens.Tokens
import kotlinx.coroutines.*

object TokensManagement {
    private const val DEFAULT_CLEAN_UP_INTERVAL = /*24 * 60 * */60 * 1000L; // Run once a day

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