package ru.steklopod.service

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import ru.steklopod.config.DatabaseFactory
import ru.steklopod.model.InsufficientBalanceException
import ru.steklopod.model.Transfer


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerServiceTest {

    private val transferService = TransferService()

    @BeforeAll
    fun init() {
        DatabaseFactory.init()
    }

    @Test
    fun `add account`() = runBlocking {
        val saved = transferService.addCustomer(100.0)
        val retrieved = transferService.getCustomer(saved.id)

        assertEquals(retrieved, saved)
    }

    @Test
    fun transfer() = runBlocking {
        val from = transferService.addCustomer(100.0)
        val to = transferService.addCustomer(100.0)

        val transfer = Transfer(from.id, to.id, 90.0)

        transferService.sendMoney(transfer)

        val fromAcount = transferService.getCustomer(from.id)
        val toAccount = transferService.getCustomer(to.id)

        assertEquals(fromAcount?.money, 10.0)
        assertEquals(toAccount?.money, 190.0)
    }

    @Test
    fun `transfer not enough money`() = runBlocking {
        val from = transferService.addCustomer(100.0)
        val to = transferService.addCustomer(100.0)

        val transfer = Transfer(from.id, to.id, 200.0)

        try {
            transferService.sendMoney(transfer)
        }
        catch (exception: InsufficientBalanceException) {
            assertEquals(exception.message, "Insufficient balance")
        }
    }


}