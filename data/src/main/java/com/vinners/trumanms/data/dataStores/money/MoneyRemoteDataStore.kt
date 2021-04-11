package com.vinners.trumanms.data.dataStores.money

import com.vinners.trumanms.data.models.money.Money
import com.vinners.trumanms.data.models.money.Transaction

interface MoneyRemoteDataStore {

    suspend fun getMoney(): Money

    suspend fun getTransaction():List<Transaction>

    suspend fun submitRedeem(amount: String)
}