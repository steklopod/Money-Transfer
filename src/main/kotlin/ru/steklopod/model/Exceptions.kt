package ru.steklopod.model

open class ApiException(
    val httpStatus: Int,
    val code: Int,
    override val message: String,
    val status: String = Status.ERROR.name
) : RuntimeException(message)

open class DomainException(code: Int = 1000, message: String) : ApiException(422, code, message)
open class ValidationException(code: Int = 1100, message: String) : ApiException(400, code, message)

class CustomerNotFoundException : DomainException(2000, "Customer not found")
class InsufficientBalanceException : DomainException(2001, "Insufficient balance")
class DeleteAccountBalanceException : DomainException(2002, "Account could not be deleted, the balance must be 0(zero)")