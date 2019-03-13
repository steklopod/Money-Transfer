package ru.steklopod

import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import ru.steklopod.config.DatabaseFactory
import ru.steklopod.model.ApiException
import ru.steklopod.model.Status.OK
import ru.steklopod.model.Transfer
import ru.steklopod.service.TransferService
import spark.Spark.*

class Main {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val gson = Gson()
            val accountService = TransferService()
            DatabaseFactory.init()
            addExceptionHandler(gson)
            addApiConfig()

            post("/transfer") { req, res ->
                val from = req.queryParams("fromId").toInt()
                val to = req.queryParams("toId").toInt()
                val amount = req.queryParams("amount").toDouble()

                runBlocking {
                    accountService.sendMoney(Transfer(from, to, amount))
                    res.status(201)
                    OK.name
                }
            }

            path("/accounts") {
                get("") { _, _ ->
                    runBlocking {
                        gson.toJson(accountService.getAllCustomers())
                    }
                }

                get("/:id") { req, res ->
                    runBlocking {
                        gson.toJson(
                            accountService.getCustomer(req.params("id").toInt())
                        )
                    }
                }

                post("/create") { req, res ->
                    runBlocking {
                        accountService.addCustomer(req.queryParams("money").toDouble())
                    }
                    res.status(201)
                    OK.name
                }

                post("/delete") { req, res ->
                    runBlocking {
                        accountService.deleteCustomer(req.queryParams("id").toInt())
                    }
                    res.status(201)
                    OK.name
                }

            }
        }

        private fun addApiConfig() {
            notFound { _, response ->
                response.status(404)
                "{\"message\":\" Oops. The route has not been found :-( \"}"
            }

            after("/*") { _, res -> res.type("application/json") } // Always set JSON as the response type
        }

        private fun addExceptionHandler(gson: Gson) {
            exception(Exception::class.java) { e, _, response ->
                val message = e.localizedMessage
                println(message)
                response.status(500)
                response.body(gson.toJson(message))
            }

            exception(ApiException::class.java) { e, _, response ->
                val message = e.message
                println(message)
                response.status(e.httpStatus)
                response.body(gson.toJson(message))
            }
        }

    }

}