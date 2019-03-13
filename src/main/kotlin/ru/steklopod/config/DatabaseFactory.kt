package ru.steklopod.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
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
        val config = HikariConfig().apply {
            driverClassName = "org.h2.Driver"
            jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
            username = "sa"
            password = ""
            validate()
        }
        return HikariDataSource(config)
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