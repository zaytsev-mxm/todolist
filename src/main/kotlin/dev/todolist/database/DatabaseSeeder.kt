package dev.todolist.database

import dev.todolist.database.users.UserDTO
import dev.todolist.database.users.Users
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object Seeding {
    
    fun seedInitialData() {
        transaction {
            seedAdminUser()
            // Add more seeding functions here
            // seedTestLists()
            // seedSampleTodos()
        }
    }
    
    private fun seedAdminUser() {
        // Check if admin already exists (by login, not just count)
        val existingAdmin = Users.fetch(login = "admin")
        
        if (existingAdmin == null) {
            val adminUser = UserDTO(
                id = UUID.randomUUID().toString(),
                login = "admin",
                password = "admin123", // Will be hashed by Users.insert()
                username = "Administrator",
                email = "admin@example.com"
            )
            
            Users.insert(adminUser)
            println("✅ Seeded admin user (login: admin, password: admin123)")
        } else {
            println("ℹ️ Admin user already exists, skipping")
        }
    }
    
    // Example: Add more seed functions
    private fun seedTestLists() {
        // Only seed test data if explicitly enabled
        if (System.getenv("DB_SEED") == "force") {
            // Seed test lists here
            println("✅ Seeded test lists")
        }
    }
}
