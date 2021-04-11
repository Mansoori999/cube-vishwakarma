package com.vinners.trumanms.feature_help.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vinners.trumanms.core.di.modules.ViewModelKey
import com.vinners.trumanms.feature_help.ui.HelpViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HelpModule {

    @Binds
    abstract fun bindViewModelFactory(factoryAuth: HelpModuleFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HelpViewModel::class)
    abstract fun bindHelpViewModel(viewModel: HelpViewModel): ViewModel
}