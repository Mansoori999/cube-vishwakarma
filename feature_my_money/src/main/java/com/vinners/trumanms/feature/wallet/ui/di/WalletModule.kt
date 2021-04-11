package com.vinners.trumanms.feature.wallet.ui.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vinners.trumanms.core.di.modules.ViewModelKey
import com.vinners.trumanms.feature.wallet.ui.ui.WalletInfoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WalletModule {
    @Binds
    abstract fun bindViewModelFactory(factoryAuth: WalletViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(WalletInfoViewModel::class)
    abstract fun bindWalletJobsViewModel(viewModel: WalletInfoViewModel): ViewModel
}