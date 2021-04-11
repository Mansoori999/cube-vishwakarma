package com.vinners.trumanms.remote.retrofitServices

import com.vinners.trumanms.data.models.money.Money
import com.vinners.trumanms.data.models.money.Transaction
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoneyService {

    @GET("api/transaction/mymoney")
    suspend fun getMoney():Response<List<Money>>

    @GET("api/transaction/history")
    suspend fun getTransactionHistory(): Response<List<Transaction>>

    @GET("api/transaction/redeem")
    suspend fun submitRedeem(@Query("amount")amount: String): Response<List<String>>
}