package com.vinners.trumanms.feature.wallet.ui.di

import com.vinners.trumanms.core.di.CoreComponent
import com.vinners.trumanms.core.di.scopes.FeatureScope
import com.vinners.trumanms.feature.wallet.ui.ui.ExclusiveOfferActivity
import com.vinners.trumanms.feature.wallet.ui.ui.ReferActivity
import com.vinners.trumanms.feature.wallet.ui.ui.transactionHistory.TranctionHistoryActivity
import com.vinners.trumanms.feature.wallet.ui.ui.WalletFragment
import dagger.Component

@FeatureScope
@Component(
    modules = [
        WalletModule::class
    ],
    dependencies = [CoreComponent::class]
)
interface WalletComponent {

    fun inject(walletFragment: WalletFragment)

    fun inject(tranctionHistoryActivity: TranctionHistoryActivity)

    fun inject(exclusiveOfferActivity: ExclusiveOfferActivity)

    fun inject(referActivity: ReferActivity)
}