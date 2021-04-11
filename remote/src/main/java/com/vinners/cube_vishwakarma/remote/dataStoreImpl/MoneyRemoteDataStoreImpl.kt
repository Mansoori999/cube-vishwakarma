package com.vinners.cube_vishwakarma.remote.dataStoreImpl

import com.vinners.cube_vishwakarma.data.dataStores.money.MoneyRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.money.Money
import com.vinners.cube_vishwakarma.data.models.money.Transaction
import com.vinners.cube_vishwakarma.remote.extensions.bodyOrThrow
import com.vinners.cube_vishwakarma.remote.retrofitServices.MoneyService
import javax.inject.Inject

class MoneyRemoteDataStoreImpl @Inject constructor(
    private val moneyService: MoneyService
): MoneyRemoteDataStore {

    override suspend fun getMoney(): Money {
        return moneyService.getMoney().bodyOrThrow().first()
    }

    override suspend fun getTransaction(): List<Transaction> {
        return moneyService.getTransactionHistory().bodyOrThrow()
    }

    override suspend fun submitRedeem(amount: String) {
        moneyService.submitRedeem(amount).bodyOrThrow()
    }
}