package ru.steklopod.model

import org.jetbrains.exposed.sql.Table


data class Customer(val id: Int, val money: Double)


object Customers : Table("customers") {
    val id = integer("id").primaryKey().autoIncrement()
    val money = double("money").check { it greaterEq 0.0}
}
