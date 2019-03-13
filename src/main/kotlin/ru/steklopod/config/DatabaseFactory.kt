package ru.steklopod.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import ru.steklopod.model.Customers

object DatabaseFactory {

    fun init() {
        Database.connect(hikari())
        addTestCustomers()
    }


    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
        config.validate()
        config.setAutoCommit(false)
        config.username = "sa"
        config.password = ""
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }


    private fun addTestCustomers() {
        transaction {
            create(Customers)

            with(Customers) {
                insert { it[money] = 100.0 }; insert { it[money] = 200.0 }
            }
        }
    }

}