package com.vinners.trumanms.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vinners.trumanms.core.di.modules.ViewModelKey
import com.vinners.trumanms.ui.appIntro.AppIntroViewModel
import com.vinners.trumanms.feature.auth.ui.dashboard.DashboardViewModel
import com.vinners.trumanms.ui.home.HomeViewModel
import com.vinners.trumanms.ui.languageSelection.LanguageViewModel
import com.vinners.trumanms.ui.splash.SplashViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(AppIntroViewModel::class)
    abstract fun bindAppIntroViewModel(viewModel: AppIntroViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

}