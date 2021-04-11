package com.vinners.cube_vishwakarma.data.dataStores.money

import com.vinners.cube_vishwakarma.data.models.money.Money
import com.vinners.cube_vishwakarma.data.models.money.Transaction

interface MoneyRemoteDataStore {

    suspend fun getMoney(): Money

    suspend fun getTransaction():List<Transaction>

    suspend fun submitRedeem(amount: String)
}