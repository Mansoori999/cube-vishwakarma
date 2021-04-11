package com.vinners.trumanms.remote.dataStoreImpl

import com.vinners.trumanms.data.dataStores.money.MoneyRemoteDataStore
import com.vinners.trumanms.data.models.money.Money
import com.vinners.trumanms.data.models.money.Transaction
import com.vinners.trumanms.remote.extensions.bodyOrThrow
import com.vinners.trumanms.remote.retrofitServices.MoneyService
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