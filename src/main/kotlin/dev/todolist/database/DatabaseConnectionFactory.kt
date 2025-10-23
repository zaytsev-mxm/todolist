package dev.todolist.database

import dev.todolist.database.lists.Lists
import dev.todolist.database.tokens.Tokens
import dev.todolist.database.users.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    
    fun init() {
        val driverClassName = "org.postgresql.Driver"
        val jdbcURL = getDbUrl()
        val dbUser = getEnv("DB_USER", "postgres")
        val dbPassword = getEnv("DB_PASSWORD", "postgres")
        
        Database.connect(
            url = jdbcURL,
            driver = driverClassName,
            user = dbUser,
            password = dbPassword
        )
        
        // Create tables if they don't exist
        transaction {
            SchemaUtils.create(Users, Tokens, Lists)
            // Note: Add the Todos table here once you find it
            // SchemaUtils.create(Users, Tokens, Lists, Todos)
        }
        
        // Seeding control based on environment
        handleSeeding()
    }

    private fun getDbUrl(): String {
        val dbHost = getEnv("DB_HOST", "localhost")
        val portDb = getEnv("PORT_DB", "5432")
        val dbName = getEnv("DB_NAME", "todolist")
        return "jdbc:postgresql://${dbHost}:${portDb}/${dbName}"
    }
    
    private fun handleSeeding() {
        val seedMode = getEnv("DB_SEED", "auto").lowercase()
        
        when (seedMode) {
            "force" -> {
                println("🌱 Forced seeding enabled")
                Seeding.seedInitialData()
            }
            "auto" -> {
                println("🌱 Auto seeding enabled (only if empty)")
                Seeding.seedInitialData()
            }
            "none", "false", "disabled" -> {
                println("🚫 Seeding disabled")
                // Do nothing
            }
            else -> {
                println("⚠️ Unknown DB_SEED value: $seedMode, defaulting to 'auto'")
                Seeding.seedInitialData()
            }
        }
    }
    
    private fun getEnv(name: String, default: String): String {
        return System.getenv(name) ?: default
    }
}
