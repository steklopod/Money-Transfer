package ru.steklopod.service

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.steklopod.config.DatabaseFactory.dbQuery
import ru.steklopod.model.*

class TransferService {

    suspend fun sendMoney(transfer: Transfer) {
        val fromId = transfer.fromId
        val toId = transfer.toId
        val amount = transfer.amount

        if (amount <= 0) throw ValidationException(message = "Not valid amount in request: $amount.")

        getCustomer(fromId)?.let {
            if (it.money > amount) {
                dbQuery {
                    transaction {
                        Customers.update({ Customers.id eq fromId }) {
                            with(SqlExpressionBuilder) {
                                it.update(Customers.money, Customers.money - amount)
                            }
                        }
                        Customers.update({ Customers.id eq toId }) {
                            with(SqlExpressionBuilder) {
                                it.update(Customers.money, Customers.money + amount)
                            }
                        }
                    }
                }
            } else {
                throw InsufficientBalanceException()
            }
        }
    }

    suspend fun getAllCustomers(): List<Customer> = dbQuery {
        Customers.selectAll().map { it.toCustomer() }
    }

    suspend fun getCustomer(id: Int): Customer? = dbQuery {
        Customers
            .select { (Customers.id eq id) }
            .mapNotNull { it.toCustomer() }
            .singleOrNull()
            ?: throw CustomerNotFoundException()
    }

    suspend fun addCustomer(money: Double): Customer {
        var key = 0
        dbQuery {
            key = (Customers.insert {
                it[this.money] = money
            } get Customers.id)!!
        }
        return getCustomer(key)!!
    }

    suspend fun deleteCustomer(id: Int): Boolean = dbQuery {
        Customers.deleteWhere { Customers.id eq id } > 0
    }

    private fun ResultRow.toCustomer() = Customer(this[Customers.id], this[Customers.money])

}
