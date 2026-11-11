
package com.bookease

import org.bson.Document
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
class MongoConnectionTest {

    @Bean
    fun testMongoConnection(
        mongoTemplate: MongoTemplate
    ) = CommandLineRunner {

        try {
            val result = mongoTemplate.executeCommand(
                Document("ping", 1)
            )

            println()
            println("================================")
            println("MONGODB CONNECTION SUCCESSFUL!")
            println("Response: $result")
            println("================================")
            println()

        } catch (e: Exception) {

            println()
            println("================================")
            println("MONGODB CONNECTION FAILED!")
            println("Reason: ${e.message}")
            println("================================")
            println()
        }
    }
}
