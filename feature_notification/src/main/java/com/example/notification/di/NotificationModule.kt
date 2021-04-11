package com.example.notification.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notification.ui.NotificationViewModel
import com.vinners.trumanms.core.di.modules.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class NotificationModule {

    @Binds
    abstract fun bindViewModelFactory(factoryAuth: NotificationViewModuleFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel::class)
    abstract fun bindNotificationViewModel(viewModel: NotificationViewModel): ViewModel
}