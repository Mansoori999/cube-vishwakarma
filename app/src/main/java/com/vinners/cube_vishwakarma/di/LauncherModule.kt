package com.vinners.cube_vishwakarma.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vinners.cube_vishwakarma.core.di.modules.ViewModelKey
import com.vinners.cube_vishwakarma.ui.languageSelection.LanguageViewModel
import com.vinners.cube_vishwakarma.ui.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class LauncherModule {

    @Binds
    abstract fun bindViewModelFactory(factoryAuth: LauncherViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindLauncherViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LanguageViewModel::class)
    abstract fun bindLanguageViewModel(viewModel: LanguageViewModel): ViewModel


}